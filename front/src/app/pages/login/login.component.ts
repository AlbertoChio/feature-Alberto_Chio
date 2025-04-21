import { Component, inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AplazoButtonComponent } from '@apz/shared-ui/button';
import { AplazoLogoComponent } from '@apz/shared-ui/logo';
import { LoginService } from '../../services/login.service';
import { catchError } from 'rxjs';
import { DialogService } from '../../services/dialog.service';
import { AuthService } from '../../services/auth.service';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [ReactiveFormsModule, AplazoButtonComponent, AplazoLogoComponent],
})
export class LoginComponent implements OnInit {
  readonly #loginService: LoginService = inject(LoginService);
  readonly #dialogService: DialogService = inject(DialogService);
  readonly #authService: AuthService = inject(AuthService);

  readonly username = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required, Validators.email],
  });

  readonly password = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });

  readonly form = new FormGroup({
    username: this.username,
    password: this.password,
  });

  ngOnInit(): void {
    this.#authService.setCurrentUser(null);
  }

  login(): void {
    this.#loginService
      .execute({
        username: this.username.value,
        password: this.password.value,
      })
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
