import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CoreModule} from './modules/core/core.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HomeModule} from './modules/home/home.module';
import {AuthModule} from './modules/auth/auth.module';
import {ClientsModule} from "./modules/clients/clients.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    CoreModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HomeModule,
    AuthModule,
    ClientsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
