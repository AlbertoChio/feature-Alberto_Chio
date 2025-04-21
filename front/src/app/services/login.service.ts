import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable, take, tap, throwError } from 'rxjs';
import { ROUTE_CONFIG } from '../config/routes.config';
import { Credentials } from '../entities/credentials';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  readonly #router = inject(Router);
  readonly #http = inject(HttpClient);
  readonly #authService: AuthService = inject(AuthService);
  readonly #apiVersion = 'v1';
  #retrieveData(credentials: { email: string; password: string }): Observable<{
    token: string;
    expiresIn: number;
  }> {
    return this.#http.post<{
      token: string;
      expiresIn: number;
    }>(
      `${environment.BACK_ENDPOINT}/${this.#apiVersion}/auth/login`,
      credentials
    );
  }

  execute(credentials: Credentials): Observable<string | never> {
    try {
      if (!credentials.username || !credentials.password) {
        throw new Error('Compruebe sus datos');
      }

      return this.#retrieveData({
        email: credentials.username,
        password: credentials.password,
      }).pipe(
        map((data) => {
          const token = data.token;

          if (!token) {
            throw new Error('Compruebe sus datos');
          }
          return token;
        }),

        tap((token) => {
          if (token) {
            this.#authService.setCurrentUser({
              userPayload: this.#authService.decodeJwt(token),
              token: token,
            });
            this.#router.navigate([ROUTE_CONFIG.register]);
          }
        }),
        take(1)
      );
    } catch (error) {
      console.warn(error);
      sessionStorage.removeItem('token');
      return throwError(() => error);
    }
  }
}
