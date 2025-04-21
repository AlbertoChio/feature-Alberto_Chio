import {
  AbstractControl,
  AsyncValidatorFn,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';
import { CustomerService } from './customer.service';
import { map, Observable, take } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CustomValidators {
  public static ageRangeValidator(minAge: number, maxAge: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (!value) return null;

      const birthDate = typeof value === 'string' ? new Date(value) : value;
      if (!birthDate) return null;

      if (!(birthDate instanceof Date) || isNaN(birthDate.getTime())) {
        return { invalidDate: true };
      }

      const today = new Date();
      const age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      const dayDiff = today.getDate() - birthDate.getDate();
      const adjustedAge =
        monthDiff < 0 || (monthDiff === 0 && dayDiff < 0) ? age - 1 : age;

      if (adjustedAge < minAge || adjustedAge > maxAge) {
        return { ageRange: { actualAge: adjustedAge, minAge, maxAge } };
      }

      return null;
    };
  }

  asyncAmountValidator(customerService: CustomerService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return customerService.getCurrentCustomer().pipe(
        take(1), // Only take the latest user once
        map((customer) => {
          const min = 100;
          const max = customer?.availableCreditLineAmount ?? 0;
          const value = control.value;

          if (value == null) return null;

          if (value < min || value > max) {
            return {
              invalidAmount: {
                requiredMin: min,
                actual: value,
                requiredMax: max,
              },
            };
          }

          return null;
        })
      );
    };
  }
}
