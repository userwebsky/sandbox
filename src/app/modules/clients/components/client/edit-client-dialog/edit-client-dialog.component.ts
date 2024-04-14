import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { Client } from "../../../../core/models/client.model";
import { Router } from "@angular/router";
import { ClientsService } from "../../../../core/services/clients.service";

@Component({
  selector: 'app-edit-client-dialog',
  templateUrl: './edit-client-dialog.component.html',
  styleUrls: ['./edit-client-dialog.component.scss']
})
export class EditClientDialogComponent {

  constructor(private dialogRef: MatDialogRef<EditClientDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { client: Client },
              private router: Router,
              private clientsService: ClientsService) {
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
