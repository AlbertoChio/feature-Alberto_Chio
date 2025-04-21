import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { DialogService } from '../../services/dialog.service';
import { CustomerService } from '../../services/customer.service';
import { CustomValidators } from '../../services/custom-validators';
import { LoanService } from '../../services/loan.service';
import { catchError, map } from 'rxjs';
import { AplazoButtonComponent } from '@apz/shared-ui/button';

@Component({
  standalone: true,
  selector: 'app-home',
  templateUrl: './home.component.html',
  imports: [ReactiveFormsModule, CommonModule,AplazoButtonComponent,],
})
export class HomeComponent implements OnInit {

  readonly #customerService: CustomerService = inject(CustomerService);
  readonly #loanService: LoanService = inject(LoanService);
  readonly #dialogService: DialogService = inject(DialogService);
  readonly #customValidators: CustomValidators = inject(CustomValidators);

  readonly limiteCredito$ = this.#customerService.getCurrentCustomer().pipe(
    map((currenCustomer)=>{
      return currenCustomer?.availableCreditLineAmount
    })
  )
  readonly amount = new FormControl<number>(100, {
    nonNullable: true,
    validators: [Validators.required],
    asyncValidators: [
      this.#customValidators.asyncAmountValidator(this.#customerService),
    ],
  });

  readonly form = new FormGroup({
    amount: this.amount,
  });

  ngOnInit(): void {
    this.#customerService.executeRetrieveCustomer().subscribe()
  }

  createLoan() {
    this.#loanService
      .executeCreateCustomer(this.form.controls.amount.value)
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
