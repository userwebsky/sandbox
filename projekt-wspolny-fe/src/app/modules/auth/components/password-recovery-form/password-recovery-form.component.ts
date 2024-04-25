import { Component, OnInit } from '@angular/core';
import { FormService } from "../../../core/services/form.service";
import { FormControl, FormGroup } from "@angular/forms";
import { ResetPasswordFrom } from "../../../core/models/forms.model";
import { ActivatedRoute, Router } from "@angular/router";
import { AuthService } from "../../../core/services/auth.service";
import { NotifierService } from "angular-notifier";

@Component({
  selector: 'app-password-recovery-form',
  templateUrl: './password-recovery-form.component.html',
  styleUrls: ['./password-recovery-form.component.scss']
})
export class PasswordRecoveryFormComponent implements OnInit {
  passwordsForm: FormGroup<ResetPasswordFrom> =
    this.formService.initResetPasswordFrom();

  uid = '';
  errorMessage: null | string = null;

  get controls(): ResetPasswordFrom {
    return this.passwordsForm.controls;
  }

  constructor(
    private formService: FormService,
    private route: ActivatedRoute,
    private authService: AuthService,
    private notifierService: NotifierService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe({
      next: (param) => {
        this.uid = param.get('uid') as string;
      },
    });
  }

  getErrorMessage(control: FormControl<string>): string {
    return this.formService.getErrorMessage(control);
  }

  onPasswdChange() {
    const { password } = this.passwordsForm.getRawValue();

    this.authService.changePassword({ password, uid: this.uid }).subscribe({
      next: () => {
        this.router.navigate(['/logowanie']);
        this.notifierService.notify(
          'success',
          'Poprawnie zmieniono hasło, możesz się zalogować.'
        );
      },
      error: (err) => {
        this.errorMessage = err;
      },
    });
  }

}
