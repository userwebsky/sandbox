import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { LoginForm, RecoveryFrom, RegisterForm, ResetPasswordFrom } from "../models/forms.model";
import { equivalentValidator } from "../../shared/validators/equivalent.validator";

@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor() { }

  initLoginForm(): FormGroup<LoginForm> {
    return new FormGroup({
      login: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
      password: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      })
    });
  }

  initPasswdRecoveryForm(): FormGroup<RecoveryFrom> {
    return new FormGroup({
      email: new FormControl("", {
        validators: [Validators.required, Validators.email],
        nonNullable: true,
      })
    });
  }

  initRegisterForm(): FormGroup<RegisterForm> {
    return new FormGroup({
      email: new FormControl("", {
        validators: [Validators.required, Validators.email],
        nonNullable: true,
      }),
      login: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
      password: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
      repeatedPassword: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
    },
      {
        validators: [equivalentValidator("password", "repeatedPassword")]
      });
  }

  initResetPasswordFrom(): FormGroup<ResetPasswordFrom> {
    return new FormGroup({
      password: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
      repeatedPassword: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
    });
  }

  getErrorMessage(control: FormControl): string {
    if (control.hasError('required')) {
      return 'Ta kontrolka jest wymagana.';
    }

    if (control.hasError('minlength')) {
      return `Minimalna ilość znaków: ${control.errors?.['minlength']?.requiredLength}.`;
    }

    if (control.hasError('maxlength')) {
      return `Maksymalna ilość znaków: ${control.errors?.['maxlength']?.requiredLength}.`;
    }

    if (control.hasError('email')) {
      return `Niepoprawny adres e-mail.`;
    }

    if (control.hasError('passwordsNotEqual')) {
      return 'Hasła muszą być takie same.';
    }

    return '';
  }
}
