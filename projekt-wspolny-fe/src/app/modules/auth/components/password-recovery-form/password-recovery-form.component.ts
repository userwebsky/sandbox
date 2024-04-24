import { Component } from '@angular/core';
import { FormService } from "../../../core/services/form.service";
import { FormControl, FormGroup } from "@angular/forms";
import { ResetPasswordFrom } from "../../../core/models/forms.model";

@Component({
  selector: 'app-password-recovery-form',
  templateUrl: './password-recovery-form.component.html',
  styleUrls: ['./password-recovery-form.component.scss']
})
export class PasswordRecoveryFormComponent {

  passwdRecoveryFrom: FormGroup<ResetPasswordFrom> = this.formService.initResetPasswordFrom();

  get controls() {
    return this.passwdRecoveryFrom.controls;
  }

  constructor(private formService: FormService) {
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }
}
