import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  map,
  Observable,
  switchMap,
  take,
  tap,
  throwError,
} from 'rxjs';
import { CreateCustomer } from '../entities/create-customer';
import { CustomerInfo } from '../entities/customer-info';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ROUTE_CONFIG } from '../config/routes.config';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  readonly #http = inject(HttpClient);
  readonly #authService: AuthService = inject(AuthService);
  readonly #router: Router = inject(Router);
  readonly #apiVersion = 'v1';

  #currentCustomer: BehaviorSubject<CustomerInfo | null> =
    new BehaviorSubject<CustomerInfo | null>(null);

  constructor() {
    const saved = localStorage.getItem('customer');
    if (saved) {
      this.#currentCustomer.next(JSON.parse(saved));
    }
  }

  setCurrentCustomer(customer: CustomerInfo | null): void {
    this.#currentCustomer.next(customer);
    localStorage.setItem('customer', JSON.stringify(customer));
  }

  clearCustomer(): void {
    this.#currentCustomer.next(null);
    localStorage.removeItem('user');
  }

  getCurrentCustomer(): Observable<CustomerInfo | null> {
    return this.#currentCustomer.asObservable();
  }

  getCustomerId(): string | null {
    return this.#currentCustomer.getValue()?.id ?? null;
  }

  #retrieveData(): Observable<CustomerInfo> {
    return this.#authService.getCurrentUser().pipe(
      switchMap((userInfo) => {
        if (!userInfo?.userPayload?.userId) {
          throw new Error('no se encontro sesion activa');
        }
        return this.#http.get<CustomerInfo>(
          `${environment.BACK_ENDPOINT}/${this.#apiVersion}/customers/${
            userInfo.userPayload.userId
          }`
        );
      })
    );
  }

  #createCustomer(createCustomer: CreateCustomer): Observable<CustomerInfo> {
    return this.#http.post<CustomerInfo>(
      `${environment.BACK_ENDPOINT}/${this.#apiVersion}/customers`,
      createCustomer
    );
  }

  executeCreateCustomer(
    createCustomer: CreateCustomer
  ): Observable<CustomerInfo> {
    try {
      return this.#createCustomer(createCustomer).pipe(
        tap((data) => {
          this.setCurrentCustomer(data);
          this.#router.navigate([ROUTE_CONFIG.app, ROUTE_CONFIG.home]);
        }),
        take(1)
      );
    } catch (error) {
      console.warn(error);
      return throwError(() => error);
    }
  }

  executeRetrieveCustomer(): Observable<CustomerInfo> {
    try {
      return this.#retrieveData().pipe(
        tap((data) => {
          this.setCurrentCustomer(data);
        }),
        take(1)
      );
    } catch (error) {
      console.warn(error);
      return throwError(() => error);
    }
  }
}
