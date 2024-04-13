import { Injectable } from '@angular/core';
import { AbstractControl } from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class FormsService {

  constructor() { }

  getErrorMessage(control: AbstractControl<any>) {
    if (control.hasError('email')) {
      return "Niepoprawny email!";
    } else if (control.hasError('required')) {
      return "To pole jest wymagane!";
    } else {
      return "Formularz zawiera błędy!";
    }
  }
}
