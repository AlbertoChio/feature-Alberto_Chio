import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { LoanService } from '../../services/loan.service';

@Component({
  standalone: true,
  selector: 'app-historial',
  templateUrl: './historial.component.html',
  imports: [CommonModule]
})
export class HistorialComponent {
  readonly #loanService: LoanService = inject(LoanService);
  readonly loanInfo$ = this.#loanService.getCurrentLoan()

}
