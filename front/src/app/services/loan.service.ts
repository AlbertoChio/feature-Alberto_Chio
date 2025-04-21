import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import {
  BehaviorSubject,
  Observable,
  switchMap,
  take,
  tap,
  throwError,
} from 'rxjs';
import { LoanInfo } from '../entities/loan-info';
import { environment } from '../../environments/environment';
import { ROUTE_CONFIG } from '../config/routes.config';

@Injectable({
  providedIn: 'root',
})
export class LoanService {
  readonly #http = inject(HttpClient);
  readonly #authService: AuthService = inject(AuthService);
  readonly #router: Router = inject(Router);
  readonly #apiVersion = 'v1';

  #currentLoan: BehaviorSubject<LoanInfo | null> =
    new BehaviorSubject<LoanInfo | null>(null);

  constructor() {
    const saved = localStorage.getItem('loan');
    if (saved) {
      this.#currentLoan.next(JSON.parse(saved));
    }
  }

  setCurrentLoan(loan: LoanInfo | null): void {
    this.#currentLoan.next(loan);
    localStorage.setItem('loan', JSON.stringify(loan));
  }

  clearloan(): void {
    this.#currentLoan.next(null);
    localStorage.removeItem('user');
  }

  getCurrentLoan(): Observable<LoanInfo | null> {
    return this.#currentLoan.asObservable();
  }

  getLoanId(): string | null {
    return this.#currentLoan.getValue()?.id ?? null;
  }

  #retrieveData(): Observable<LoanInfo> {
    return this.#authService.getCurrentUser().pipe(
      switchMap((userInfo) => {
        if (!userInfo?.userPayload?.userId) {
          throw new Error('no se encontro sesion activa');
        }
        return this.#http.get<LoanInfo>(
          `${environment.BACK_ENDPOINT}/${this.#apiVersion}/loans/${
            userInfo?.userPayload?.userId
          }`
        );
      })
    );
  }

  #createLoan(amount: number): Observable<LoanInfo> {
    return this.#authService.getCurrentUser().pipe(
      switchMap((userInfo) => {
        if (!userInfo?.userPayload?.userId) {
          throw new Error('no se encontro sesion activa');
        }
        return this.#http.post<LoanInfo>(
          `${environment.BACK_ENDPOINT}/${this.#apiVersion}/loans`,
          { amount: amount, customerId: userInfo?.userPayload?.userId }
        );
      })
    );
  }

  executeCreateCustomer(amount: number): Observable<LoanInfo> {
    try {
      return this.#createLoan(amount).pipe(
        tap((data) => {
          this.setCurrentLoan(data);
          this.#router.navigate([ROUTE_CONFIG.app, ROUTE_CONFIG.historial]);
        }),
        take(1)
      );
    } catch (error) {
      console.warn(error);
      return throwError(() => error);
    }
  }

  executeRetrieveCustomer(): Observable<LoanInfo> {
    try {
      return this.#retrieveData().pipe(
        tap((data) => {
          this.setCurrentLoan(data);
        }),
        take(1)
      );
    } catch (error) {
      console.warn(error);
      return throwError(() => error);
    }
  }
}
