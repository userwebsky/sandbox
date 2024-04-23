import { Component, OnDestroy, OnInit, } from '@angular/core';
import { Todo } from "../shared/interfaces/todo.interface";
import { TodoService } from "../core/services/todo.service";
import { Subscription } from "rxjs";
import { TodoApiService } from "../core/services/todo-api.service";
import { Store } from "@ngrx/store";
import { AppState } from "../store/app.reducer";
import * as TodoListActions from './store/todo-list.action';
import { selectTodoListTodosState } from "./store/todo-list.selector";

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.css']
})
//  implements AfterViewInit, AfterViewChecked
export class TodoListComponent implements OnInit, OnDestroy {
  // todos: Todo[] = this.todoService.todos;
  todos: Todo[] = [];
  errorMessage: null | string = null;
  sub!: Subscription;

  constructor(
    private todoService: TodoService,
    private todoApiService: TodoApiService,
    private store: Store<AppState>
  ) {
  }


  ngOnInit(): void {
    // this.sub = this.todoService.todoChanged.subscribe({
    //   next: arrTodos => this.todos = arrTodos
    // });
    //
    // if (this.todos.length === 0) {
    //   this.todoApiService.getTodos().subscribe({
    //     error: err => {
    //       this.errorMessage = 'Wystąpił błąd. Spróbuj ponownie.'
    //     }
    //   })
    // }
    this.store.dispatch(TodoListActions.fetchTodos());

    this.sub = this.store.select(selectTodoListTodosState).subscribe({
      next: ({todos, loading, errorMsg}) => {
        console.log('test', todos, loading, errorMsg)
        this.todos = [...todos];
        this.errorMessage = errorMsg;
        // loading
      }
    })
  }


  addTodo(todo: string): void {
    // this.todoApiService.postTodo({name: todo, isComplete: false}).subscribe({
    //   error: err => {
    //     this.errorMessage = 'Wystąpił błąd. Spróbuj ponownie.'
    //   }
    // });
    const id = this.todos[this.todos.length - 1].id + 1;
    this.store.dispatch(TodoListActions.addTodo({todo: {name: todo, isComplete: false}}))
  }

  clearErrorMessage() {
    this.errorMessage = '';
  }

  deleteTodo(id: number) {
    // this.todoApiService.deleteTodo(id).subscribe({
    //   error: err => {
    //     this.errorMessage = 'Wystąpił błąd. Spróbuj ponownie.'
    //   }
    // })
    this.store.dispatch(TodoListActions.deleteTodo({id}))
  }

  changeTodoStatus(id: number, todo: Todo) {
    // this.todoApiService.patchTodo(id, {isComplete: !todo.isComplete}).subscribe({
    //   error: err => {
    //     this.errorMessage = 'Wystąpił błąd. Spróbuj ponownie.'
    //   }
    // });
    this.store.dispatch(TodoListActions.changeTodoStatus({id}))
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
