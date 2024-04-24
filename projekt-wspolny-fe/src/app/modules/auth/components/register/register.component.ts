import { Component, OnDestroy } from '@angular/core';
import { FormService } from "../../../core/services/form.service";
import { FormControl, FormGroup } from "@angular/forms";
import { RegisterForm } from "../../../core/models/forms.model";
import * as AuthActions from '../../store/auth.actions'
import { AppState } from "../../../../store/app.reducer";
import { Store } from "@ngrx/store";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy {
  registerForm: FormGroup<RegisterForm> = this.formService.initRegisterForm();
  notMatchingPaswswordsError: null | string = null;

  get controls() {
    return this.registerForm.controls;
  }

  constructor(private formService: FormService, private store: Store<AppState>) {
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }

  onRegister() {
    const {login, email, password, repeatedPassword} = this.registerForm.getRawValue();

    if(password !== repeatedPassword) {
      this.notMatchingPaswswordsError = 'Passwords do not match';
      return;
    }

    this.store.dispatch(AuthActions.register({registerData: {login, email, password}}))
  }

  ngOnDestroy(): void {
    this.store.dispatch(AuthActions.clearError())
  }
}
