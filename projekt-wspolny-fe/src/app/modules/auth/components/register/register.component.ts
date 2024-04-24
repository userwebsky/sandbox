import { Component } from '@angular/core';
import { FormService } from "../../../core/services/form.service";
import { FormControl, FormGroup } from "@angular/forms";
import { RegisterForm } from "../../../core/models/forms.model";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup<RegisterForm> = this.formService.initRegisterForm();

  get controls() {
    return this.registerForm.controls;
  }

  constructor(private formService: FormService) {
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }
}
