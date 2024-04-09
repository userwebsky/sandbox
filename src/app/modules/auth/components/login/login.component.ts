import {Component} from '@angular/core';
import {AuthService} from "../../../core/services/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  hide = true;
  userData = {
    username: '',
    password: ''
  }

  errorMessage = '';

  constructor(private authService: AuthService) {

  }

  onLogin() {
    this.authService.login(this.userData).subscribe({
      next: result => {
        if (result.length === 0) {
          this.errorMessage = 'Podano nie prrawidłowe dane logowania';
        }
      },
      error: (err) => {
        this.errorMessage = 'Wystąpił błąd.';
      },
    });
  }
}
