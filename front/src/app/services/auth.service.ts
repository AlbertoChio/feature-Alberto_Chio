import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserTokenPayload } from '../entities/user-token-payload';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  #currentUser: BehaviorSubject<{
    userPayload: UserTokenPayload;
    token: string;
  } | null> = new BehaviorSubject<{
    userPayload: UserTokenPayload;
    token: string;
  } | null>(null);

  constructor() {
    const saved = localStorage.getItem('user');
    if (saved) {
      this.#currentUser.next(JSON.parse(saved));
    }
  }

  setCurrentUser(user: {
    userPayload: UserTokenPayload;
    token: string;
  } | null ):void {
    this.#currentUser.next(user);
    localStorage.setItem('user', JSON.stringify(user));
  }

  clearUser():void {
    this.#currentUser.next(null);
    localStorage.removeItem('user');
  }

  getCurrentUser(): Observable<{
    userPayload: UserTokenPayload;
    token: string;
  } | null>  {
    return this.#currentUser.asObservable();
  }

  getToken(): string | null {
    return this.#currentUser.getValue()?.token ?? null;
  }

  decodeJwt(token: string): {
    role: 'ROLE_CUSTOMER' | 'ROLE_UNKNOWN';
    name: 'Unknown';
    userId: string;
    sub: string;
    iat: Date;
    exp: Date;
  } {
    const payload = token.split('.')[1]; // El segundo segmento es el payload
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
        .join('')
    );
    return JSON.parse(jsonPayload);
  }
}
