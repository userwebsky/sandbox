import { NgModule } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { RouterLink, RouterLinkActive } from "@angular/router";
import { SharedModule } from "../shared/shared.module";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { ErrorHandlingInterceptor } from "./interceptors/error-handling.interceptor";

@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    SharedModule,
    RouterLink,
    RouterLinkActive,
    HttpClientModule
  ],
  exports: [
    HeaderComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlingInterceptor,
      multi: true,
    },
  ],

})
export class CoreModule { }
