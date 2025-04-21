import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AplazoButtonComponent } from '@apz/shared-ui/button';
import { AplazoLogoComponent } from '@apz/shared-ui/logo';
import { CustomerService } from '../../services/customer.service';
import { CreateCustomer } from '../../entities/create-customer';
import { CustomValidators } from '../../services/custom-validators';
import { catchError } from 'rxjs';
import { DialogService } from '../../services/dialog.service';

@Component({
  standalone: true,
  selector: 'app-register',
  templateUrl: './register.component.html',
  imports: [
    ReactiveFormsModule,
    AplazoButtonComponent,
    AplazoLogoComponent,
    CommonModule,
  ],
})
export class RegisterComponent {
  readonly #customerService: CustomerService = inject(CustomerService);
  readonly #dialogService: DialogService = inject(DialogService);

  readonly firstName = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });

  readonly lastName = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });

  readonly secondLastName = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });

  readonly dateOfBirth = new FormControl<Date>(new Date(), {
    nonNullable: true,
    validators: [
      Validators.required,
      CustomValidators.ageRangeValidator(18, 65),
    ],
  });

  readonly gender = new FormControl<'Hombre' | 'Mujer' | null>(null, {
    nonNullable: true,
    validators: [Validators.pattern(/^$|^(Hombre|Mujer)$/)],
  });

  readonly placeOfBirth = new FormControl<'CDMX' | 'SONORA' | null>(null, {
    nonNullable: true,
    validators: [Validators.pattern(/^$|^(CDMX|SONORA)$/)],
  });

  readonly curp = new FormControl<string | null>(null, {
    nonNullable: true,
    validators: [
      Validators.pattern(/^[A-Z]{4}\d{6}[HM][A-Z]{2}[A-Z]{3}[A-Z\d]\d$/),
    ],
  });

  readonly form = new FormGroup({
    firstName: this.firstName,
    lastName: this.lastName,
    secondLastName: this.secondLastName,
    dateOfBirth: this.dateOfBirth,
    gender: this.gender,
    placeOfBirth: this.placeOfBirth,
    curp: this.curp,
  });

  register(): void {
    const createCustomer: CreateCustomer = {
      firstName: this.form.controls.firstName.value,
      lastName: this.form.controls.lastName.value,
      secondLastName: this.form.controls.secondLastName.value,
      dateOfBirth: this.form.controls.dateOfBirth.value,
      gender: this.form.controls.gender.value,
      placeOfBirth: this.form.controls.placeOfBirth.value,
      curp: this.form.controls.curp.value,
    };
    this.#customerService
      .executeCreateCustomer(createCustomer)
      .pipe(
        catchError((error) => {
          if (error?.error?.message) {
            throw 'Detalle: ' + error.error.message;
          } else if (error?.message) {
            throw 'Detalle: ' + error.message;
          }
          throw 'sin informacion';
        })
      )
      .subscribe({
        error: (error) => {
          this.#dialogService.setDialog({
            show: true,
            title: 'A ocurrido un Error',
            description: error,
          });
        },
      });
  }
}
