import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { Client } from "../models/client.model";
import { ClientsService } from "../services/clients.service";

@Injectable({
  providedIn: 'root'
})
export class ClientResolver implements Resolve<Client> {
  constructor(private clientService: ClientsService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Client> {
    return this.clientService.getClient(1);
  }
}
