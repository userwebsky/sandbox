import { FormControl } from "@angular/forms";

export interface ClientResponse {
  id: number;
  firstname: string;
  surname: string;
  email: string;
  phone: string;
  address: string;
  postcode: string;
}

export type PostClient = Omit<ClientResponse, 'id'>;

export class Client implements ClientResponse {
  address: string;
  email: string;
  firstname: string;
  id: number;
  phone: string;
  postcode: string;
  surname: string;

  constructor(address: string, email: string, firstname: string, id: number, phone: string, postcode: string, surname: string) {
    this.address = address;
    this.email = email;
    this.firstname = firstname;
    this.id = id;
    this.phone = phone;
    this.postcode = postcode;
    this.surname = surname;
  }
}

export interface GetClientsResponse {
  clients: Client[];
  totalCount: number;
}

export interface PostClientForm {firstname: FormControl<string>, address: FormControl<string>, phone: FormControl<string>,
  surname: FormControl<string>, postcode: FormControl<string>, email: FormControl<string>}
