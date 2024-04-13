import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClientsRoutingModule } from './clients-routing.module';
import { ClientsComponent } from './clients.component';
import { ClientsTableComponent } from './components/clients-table/clients-table.component';
import { SharedModule } from "../shared/shared.module";
import { ClientComponent } from './components/client/client.component';


@NgModule({
  declarations: [
    ClientsComponent,
    ClientsTableComponent,
    ClientComponent
  ],
    imports: [
        CommonModule,
        ClientsRoutingModule,
        SharedModule
    ],
  exports: [
    ClientsComponent
  ]
})
export class ClientsModule {
}
