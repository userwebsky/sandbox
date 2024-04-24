import { FormControl } from "@angular/forms";

export interface LoginForm {
  password: FormControl<string>;
  login: FormControl<string>;
}

export interface RegisterForm {
  password: FormControl<string>;
  login: FormControl<string>;
  repeatedPassword: FormControl<string>;
  email: FormControl<string>;
}

export interface RecoveryFrom {
  email: FormControl<string>;
}

export interface ResetPasswordFrom {
  password: FormControl<string>;
  repeatedPassword: FormControl<string>;
}
