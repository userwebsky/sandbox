import { Component } from '@angular/core';
import { FormService } from "../../../core/services/form.service";
import { FormControl, FormGroup } from "@angular/forms";
import { RecoveryFrom } from "../../../core/models/forms.model";

@Component({
  selector: 'app-password-recovery',
  templateUrl: './password-recovery.component.html',
  styleUrls: ['./password-recovery.component.scss']
})
export class PasswordRecoveryComponent {
  passwordsForm: FormGroup<RecoveryFrom> = this.formService.initPasswdRecoveryForm();

  get controls() {
    return this.passwordsForm.controls;
  }

  constructor(private formService: FormService) {
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }
}
