import { AbstractControl, ValidationErrors } from "@angular/forms";

export class ClientValidators {
  static postcode(control: AbstractControl): ValidationErrors | null {
    const postcodePattern = /^\d{2}-\d{3}$/;
    const value = control.value;

    if (!value || postcodePattern.test(value)) { //valid
      return null;
    }

    return {invalidPostcode: {value}}; //invalid
  }
}
