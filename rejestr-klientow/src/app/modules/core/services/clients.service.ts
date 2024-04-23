import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { map, Observable } from "rxjs";
import { Client, ClientResponse, GetClientsResponse, PostClient } from "../models/client.model";

@Injectable({
  providedIn: 'root'
})
export class ClientsService {

  apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  getClients(
    pageIndex: number,
    itemsPerPage: number,
    sortDirection: string,
    sortColumnName: string,
    value = ''
  ): Observable<GetClientsResponse> {
    let params = new HttpParams().append('_page', pageIndex).append('_limit', itemsPerPage);

    if (sortColumnName) {
      params = params
        .append('_sort', sortColumnName)
        .append('_order', sortDirection);
    }

    if (value) {
      params = params.append('firstname_like', value);
    }

    return this.http.get<ClientResponse[]>(`${this.apiUrl}/clients`, {
      observe: 'response',
      params
    }).pipe(
      map((response) => {
        if (!response.body) return {clients: [], totalCount: 0};

        const clientsArr: Client[] = response.body.map(
          ({
             address,
             email,
             firstname,
             id,
             phone,
             postcode,
             surname
           }) => new Client(address, email, firstname, id, phone, postcode, surname)
        );

        const totalCount = Number(response.headers.get('X-Total-Count'));

        return {clients: clientsArr, totalCount}
      })
    )
  }

  getClient(id: number): Observable<Client> {
    return this.http.get<ClientResponse>(`${this.apiUrl}/clients/${id}`, {}).pipe(
      map(({
             address,
             email,
             firstname,
             id,
             phone,
             postcode,
             surname
           }) => new Client(address, email, firstname, id, phone, postcode, surname))
    )
  }

  postClient(clientData: PostClient): Observable<Client> {
    return this.http.post<ClientResponse>(`${this.apiUrl}/clients`, clientData).pipe(
      map(({
             address,
             email,
             firstname,
             id,
             phone,
             postcode,
             surname
           }) => new Client(address, email, firstname, id, phone, postcode, surname))
    )
  }

  deleteClient(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(`${this.apiUrl}/clients/${id}`);
  }

  putClient(clientData: PostClient, id: number): Observable<Client> {
    return this.http.put<ClientResponse>(`${this.apiUrl}/clients/${id}`, clientData).pipe(
      map(({
             address,
             email,
             firstname,
             id,
             phone,
             postcode,
             surname
           }) => new Client(address, email, firstname, id, phone, postcode, surname))
    )
  }
}
