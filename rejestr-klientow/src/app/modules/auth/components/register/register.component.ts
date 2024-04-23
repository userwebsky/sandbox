import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../../core/services/auth.service";
import { Router } from "@angular/router";
import { FormsService } from "../../../core/services/forms.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  hide = true;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router, private formService: FormsService) {
  }

  registerForm: FormGroup = new FormGroup({
    email: new FormControl('', {validators: [Validators.email], nonNullable: true}),
    username: new FormControl('', {validators: [Validators.required], nonNullable: true}),
    password: new FormControl('', {validators: [Validators.required], nonNullable: true}),
  });

  get controls() {
    return this.registerForm.controls;
  }

  ngOnInit(): void {
    this.registerForm.controls['email'].valueChanges.subscribe((text) => {
      console.log(text);
    });
  }

  onRegister() {
    this.authService.register(this.registerForm.getRawValue()).subscribe(
      {
        next: () => {
          this.router.navigate(['/logowanie']);
        },
        error: error => {
          this.errorMessage = 'Wystąpił błąd.';
        }
      }
    );
  }

  getErrorMessage(control: AbstractControl<any>) {
    return this.formService.getErrorMessage(control);
  }
}
