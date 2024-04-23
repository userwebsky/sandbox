import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { Client } from "../../../../core/models/client.model";
import { Router } from "@angular/router";
import { ClientsService } from "../../../../core/services/clients.service";

@Component({
  selector: 'app-delete-client-dialog',
  templateUrl: './delete-client-dialog.component.html',
  styleUrls: ['./delete-client-dialog.component.scss']
})
export class DeleteClientDialogComponent implements OnInit {

  client!: Client;
  errorMessage = '';

  constructor(private dialogRef: MatDialogRef<DeleteClientDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: { client: Client },
              private router: Router,
              private clientsService: ClientsService) {
  }

  ngOnInit(): void {
    this.client = this.data.client;
  }

  onDelete() {
    this.clientsService.deleteClient(this.client.id).subscribe({
      next: () => {
        this.dialogRef.close();
        this.router.navigate(['/klienci']);
      },
      error: err => {
        this.errorMessage = "Błąd!";
      }
    })
  }
}
