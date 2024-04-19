import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from "./material/material.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { AlertComponent } from './components/alert/alert.component';
import { HttpClientModule } from "@angular/common/http";
import { PhoneControlComponent } from './controls/phone-control/phone-control.component';


@NgModule({
  declarations: [
    AlertComponent,
    PhoneControlComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaterialModule
  ],
  exports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    AlertComponent,
    HttpClientModule,
    PhoneControlComponent
  ]
})
export class SharedModule {
}
