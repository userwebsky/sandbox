import { AfterViewInit, Component, OnDestroy, ViewChild } from '@angular/core';
import { ClientsService } from "../../../core/services/clients.service";
import { Client } from "../../../core/models/client.model";
import { MatTableDataSource } from "@angular/material/table";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { debounceTime, distinctUntilChanged, map, merge, startWith, Subscription, switchMap } from "rxjs";
import { FormControl } from "@angular/forms";

@Component({
  selector: 'app-clients-table',
  templateUrl: './clients-table.component.html',
  styleUrls: ['./clients-table.component.scss']
})
export class ClientsTableComponent implements AfterViewInit, OnDestroy {

  displayedColumns: string[] = ['lp', 'firstname', 'surname', 'email', 'buttons'];
  dataSource!: MatTableDataSource<Client>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  totalCount = 0;
  filterValue = new FormControl('', { nonNullable: true })
  sub = new Subscription();

  constructor(private clientsService: ClientsService) {
  }

  ngAfterViewInit(): void {
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    this.sub.add(merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          //this.isLoadingResults = true; todo that loading
          const pageIndex = this.paginator.pageIndex + 1;
          const itemsPerPage = this.paginator.pageSize;
          const sortDirection = this.sort.direction;
          const sortColumnName = this.sort.active;

          return this.clientsService.getClients(pageIndex, itemsPerPage, sortDirection, sortColumnName);
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          //this.isLoadingResults = false; todo that loading

          this.totalCount = data.totalCount;
          return data.clients;
        }),
      )
      .subscribe(clients => {
        this.dataSource = new MatTableDataSource<Client>(clients);
      }));

    this.sub.add(this.filterValue.valueChanges
      .pipe(debounceTime(500), distinctUntilChanged())
      .subscribe((value) => {
      const val = value?.trim();
      this.applyFilter(val);
    }));
  }

  applyFilter(val: string) {
    const pageIndex = this.paginator.pageIndex + 1;
    const itemsPerPage = this.paginator.pageSize;
    const sortDirection = this.sort.direction;
    const sortColumnName = this.sort.active;

    this.clientsService.getClients(pageIndex, itemsPerPage, sortDirection, sortColumnName, val).subscribe(
      {
        next: (resp) => {
          this.totalCount = resp.totalCount;
          this.dataSource = new MatTableDataSource<Client>(resp.clients);
        },
      }
    );

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
