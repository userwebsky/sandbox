import { NgModule } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from "../shared/shared.module";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { SpinnerComponent } from './components/spinner/spinner.component';
import { SpinnerInterceptor } from "./iterceptors/spinner.interceptor";
import { HeaderClassInterceptor } from "./iterceptors/header.class.interceptor";

@NgModule({
  declarations: [
    HeaderComponent,
    SpinnerComponent
  ],
  imports: [
    SharedModule,
    RouterModule,
    BrowserAnimationsModule,
    HttpClientModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HeaderClassInterceptor, multi: true }
  ],
  exports: [
    HeaderComponent,
    SpinnerComponent
  ]
})
export class CoreModule {
}
