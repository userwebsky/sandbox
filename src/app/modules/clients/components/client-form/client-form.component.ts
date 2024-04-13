import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { PostClientForm } from "../../../core/models/client.model";
import { FormsService } from "../../../core/services/forms.service";
import { ClientsService } from "../../../core/services/clients.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-client-form',
  templateUrl: './client-form.component.html',
  styleUrls: ['./client-form.component.scss']
})
export class ClientFormComponent implements OnInit {
  clientForm!: FormGroup<PostClientForm>;
  errorMessage = '';

  constructor(private formService: FormsService, private clientsService: ClientsService, private router: Router) {
  }

  get controls() {
    return this.clientForm.controls;
  }

  ngOnInit(): void {
    this.initForm();
  }

  private initForm() {
    this.clientForm = new FormGroup({
      firstname: new FormControl('', {nonNullable:true, validators: [Validators.required]}),
      surname: new FormControl('', {nonNullable:true, validators: [Validators.required]}),
      email: new FormControl('', {nonNullable:true, validators: [Validators.required, Validators.email]}),
      phone: new FormControl('', {nonNullable:true, validators: [Validators.required]}),
      address: new FormControl('', {nonNullable:true, validators: [Validators.required]}),
      postcode: new FormControl('', {nonNullable:true, validators: [Validators.required]}),
    })
  }

  onAddClient() {
    this.clientsService.postClient(this.clientForm.getRawValue()).subscribe({
      next: () => {
        this.errorMessage = '';
        this.router.navigate(['/klienci']);
      },
      error: err => {
        this.errorMessage = 'Błąd!';
      }
    })
  }

  getErrorMessage(control: FormControl<string>) {
    return this.formService.getErrorMessage(control);
  }
}
