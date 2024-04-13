import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientsComponent } from "./clients.component";
import { ClientComponent } from "./components/client/client.component";
import { ClientFormComponent } from "./components/client-form/client-form.component";

const routes: Routes = [
  {
    path: 'klienci', component: ClientsComponent
  },
  {
    path: 'klienci/dodaj', component: ClientFormComponent
  },
  {
    path: 'klienci/:id',
    component: ClientComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientsRoutingModule {
}
