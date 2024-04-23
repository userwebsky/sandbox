import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { TodoApiService } from "../../core/services/todo-api.service";
import * as TodoListActions from './todo-list.action';
import { catchError, map, of, switchMap } from "rxjs";

@Injectable()
export class TodoListEffects {

  fetchTodos$ = createEffect(() =>
    this.actions$.pipe(
      ofType(TodoListActions.fetchTodos),
      switchMap(action => {
        return this.todoApiService.getTodos()
          .pipe(
            map((todos) => TodoListActions.fetchTodosSuccess({todos})),
            catchError(err => of(TodoListActions.fetchTodosFailed({errorMessage: 'Wystąpił błąd.'})))
          )
      })
    )
  )

  // addTodo$ = createEffect(() =>
  //   this.actions$.pipe(
  //     ofType(TodoListActions.addTodo),
  //     switchMap(action => {
  //       return this.todoApiService.postTodo(action.todo)
  //         .pipe(
  //           map((todo) => TodoListActions.addTodoSuccess({todo})),
  //           catchError(err => of(TodoListActions.addTodoFailed({errorMessage: 'Wystąpił błąd.'})))
  //         )
  //     })
  //   )
  // )

  constructor(
    private actions$: Actions,
    private todoApiService: TodoApiService
  ) {
  }
}

