import { Component, forwardRef, OnDestroy } from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR, Validators } from "@angular/forms";
import { combineLatest, Subscription } from "rxjs";
import { FormsService } from "../../../core/services/forms.service";

@Component({
  selector: 'app-phone-control',
  templateUrl: './phone-control.component.html',
  styleUrls: ['./phone-control.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: PhoneControlComponent,
      multi: true
    }
  ]
})
export class PhoneControlComponent implements ControlValueAccessor, OnDestroy {
  numberPrefixControl = new FormControl('', [Validators.required]);
  numberControl = new FormControl('', [Validators.required]);
  sub = new Subscription();

  constructor() {
    this.sub.add(combineLatest([
      this.numberPrefixControl.valueChanges,
      this.numberControl.valueChanges
    ]).subscribe(([prefix, number]) => {
      if (prefix && number) {
        this.onChange(`+${prefix}${number}`)
      } else {
        this.onChange(null);
      }
    }));
  }

  onChange = (value: string | null) => {};
  onTouch = () => {};

  registerOnChange(fn: () => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouch = fn;
  }

  setDisabledState(isDisabled: boolean): void {//wyłaczanie kontrolki, wywoła się wtedy kiedy z poziomu formularza użyjemy disable
    if (isDisabled) {
      this.numberControl.disable();
      this.numberPrefixControl.disable();
    } else {
      this.numberControl.enable();
      this.numberPrefixControl.enable();
    }
  }

  writeValue(value: any): void {//wywoła się wtedy kiedy użyjemy setValue na phone
    const valueWithoutPlus = value.replace("+", "");
    const prefix = valueWithoutPlus.slice(0, 2);
    const number = valueWithoutPlus.slice(2);

    this.numberPrefixControl.setValue(prefix);
    this.numberControl.setValue(number);
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
