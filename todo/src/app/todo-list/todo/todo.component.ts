import { Component, EventEmitter, Input, Output, } from '@angular/core';
import { Todo } from "../../shared/interfaces/todo.interface";
import { ActivatedRoute, NavigationExtras, Router } from "@angular/router";

@Component({
  selector: 'app-todo',
  templateUrl: './todo.component.html',
  styleUrls: ['./todo.component.css']
})
// implements OnChanges
// implements OnInit
export class TodoComponent {
  @Input() todo!: Todo;
  @Input() id!: number;
  @Input() i!: number;
  @Output() delete = new EventEmitter<void>();
  @Output() changeStatus = new EventEmitter<number>();
  // @ViewChild('li') li!: ElementRef;
  openModal = false;


  constructor(private router: Router, private route: ActivatedRoute) {
  }

// ngOnInit(): void {
//   this.timeout = setTimeout(() => {
//     console.log('setTImeout')
//   }, 3000)
// }

  // ngOnChanges(changes: SimpleChanges): void {
  //   console.log(changes)
  // }
  //
  //  ngDoCheck(): void {
  //    console.log('ngDoCheck został wykonany!')
  //  }

  // ngAfterViewInit(): void {
  //   console.log(this.li)
  // }

  // ngOnDestroy(): void {
  //   console.log('test')
  //   clearTimeout(this.timeout);
  // }

  changeTodoStatus() {
    this.changeStatus.emit(this.id);
  }

  toggleModal(): void {
    this.openModal = !this.openModal;
  }

  deleteTodo() {
    this.delete.emit();
  }

  navigateToDetails() {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      // state: { example: 'test' }
      // queryParams: {id: this.i, test: 'wartosc'}
    }
    this.router.navigate([this.id], navigationExtras)
  }
}
