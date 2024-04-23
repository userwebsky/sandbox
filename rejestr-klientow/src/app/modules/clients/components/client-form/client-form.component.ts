import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Client, PostClientForm } from "../../../core/models/client.model";
import { FormsService } from "../../../core/services/forms.service";
import { ClientsService } from "../../../core/services/clients.service";
import { Router } from "@angular/router";
import { Observer } from "rxjs";
import { postcodeValidator } from "../../../shared/validators/postcode.validator";

@Component({
  selector: 'app-client-form',
  templateUrl: './client-form.component.html',
  styleUrls: ['./client-form.component.scss']
})
export class ClientFormComponent implements OnInit {
  clientForm!: FormGroup<PostClientForm>;
  errorMessage = '';
  @Input() editMode = false;
  @Input() client!: Client;
  @Output() closeDialog = new EventEmitter<void>();
  observer: Observer<unknown> = {
    next: () => {
      if (this.editMode) {
        this.emitCloseDialog();
      }
      this.errorMessage = '';
      this.router.navigate(['/klienci']);
    },
    error: err => {
      this.errorMessage = 'Błąd!';
    },
    complete: () => {
    }
  };

  constructor(private formService: FormsService, private clientsService: ClientsService, private router: Router) {
  }

  get controls() {
    return this.clientForm.controls;
  }

  ngOnInit(): void {
    this.initForm();
  }

  onAddClient() {
    if (this.editMode) {
      this.clientsService.putClient(this.clientForm.getRawValue(), this.client.id)
        .subscribe(this.observer);
      return;
    }
    this.clientsService.postClient(this.clientForm.getRawValue()).subscribe(this.observer);
  }

  getErrorMessage(control: FormControl<string>) {
    return this.formService.getErrorMessage(control);
  }

  emitCloseDialog() {
    this.closeDialog.emit();
  }

  private initForm() {
    this.clientForm = new FormGroup({
      firstname: new FormControl(this.editMode ? this.client.firstname : '', {
        nonNullable: true,
        validators: [Validators.required]
      }),
      surname: new FormControl(this.editMode ? this.client.surname : '', {
        nonNullable: true,
        validators: [Validators.required]
      }),
      email: new FormControl(this.editMode ? this.client.email : '', {
        nonNullable: true,
        validators: [Validators.required, Validators.email]
      }),
      phone: new FormControl(this.editMode ? this.client.phone : '', {
        nonNullable: true,
        validators: [Validators.required]
      }),
      address: new FormControl(this.editMode ? this.client.address : '', {
        nonNullable: true,
        validators: [Validators.required]
      }),
      postcode: new FormControl(this.editMode ? this.client.postcode : '', {
        nonNullable: true,
        validators: [Validators.required, postcodeValidator()]
      }),
    })
  }
}
