import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { Todo } from "../../shared/interfaces/todo.interface";
import { TodoService } from "./todo.service";
import { Store } from "@ngrx/store";
import { AppState } from "../../store/app.reducer";

@Injectable({
  providedIn: 'root'
})
export class TodoApiService {

  constructor(private http: HttpClient, private todoService: TodoService, private store: Store<AppState>) {
  }

  getTodos(): Observable<Todo[]> {
    const headers = new HttpHeaders({
      'Authorization': 'bearer test',
      'TestowyNaglowek': 'test'
    })

    // const params = new HttpParams()
    //   .set('_limit', 1);

    return this.http.get<Todo[]>('http://localhost:3000/todo', {
      headers,
      // responseType:
      // params
    })
    //   .pipe(
    //   tap((todos) => this.store.dispatch(TodosActions.fetchTodosSuccess({todos})))
    // );
  }

  getTodo(id: number): Observable<Todo> {
    return this.http.get<Todo>(`http://localhost:3000/todo/${id}`);
  }

  postTodo(todo: Omit<Todo, "id">): Observable<Todo> {
    return this.http.post<Todo>('http://localhost:3000/todo', todo).pipe(
      tap((todo) => this.todoService.addTodo(todo))
    );
  }

  deleteTodo(id: number): Observable<{}> {
    return this.http.delete<{}>(`http://localhost:3000/todo/${id}`).pipe(
      tap(() => this.todoService.deleteTodo(id))
    );
  }

  patchTodo(id: number, todo: Omit<Todo, "id" | "name">): Observable<Todo> {
    return this.http.patch<Todo>(`http://localhost:3000/todo/${id}`, todo).pipe(
      tap((todo) => this.todoService.changeTodoStatus(todo.id, todo.isComplete))
    );
  }
}
