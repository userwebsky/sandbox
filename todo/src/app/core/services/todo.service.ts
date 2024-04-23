import { Injectable } from '@angular/core';
import { Todo } from "../../shared/interfaces/todo.interface";
import { Subject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  todoChanged = new Subject<Todo[]>();

  constructor() {
  }

  // private _todos: Todo[] = JSON.parse(localStorage.getItem('todos')!) ?? [];
  private _todos: Todo[] = [];

  public get todos() {
    return this._todos.slice();
  }

  public set todos(arrTodos: Todo[]) {
    this._todos = [...arrTodos];
    this.todoChanged.next(this.todos)
  }

  addTodo(todo: Todo): void {
    this._todos.push(todo);
    this.todoChanged.next(this.todos);
  }

  deleteTodo(id: number) {
    this._todos = this.todos.filter((todo, index) => todo.id !== id)
    this.todoChanged.next(this.todos);
  }

  changeTodoStatus(id: number, isComplete: boolean) {
    const searchedTodo = this.todos.find(todo => todo.id === id);
    if (searchedTodo) {
      searchedTodo.isComplete = isComplete;
    }
    this.todoChanged.next(this.todos);
  }

  // saveToLocalStorage() {
  //   localStorage.setItem('todos', JSON.stringify(this.todos));
  // }
}
