import { Actions, createEffect, ofType } from "@ngrx/effects";
import { inject } from "@angular/core";
import { TodoApiService } from "../../core/services/todo-api.service";
import * as TodoListActions from "./todo-list.action";
import { catchError, map, of, switchMap } from "rxjs";

export const addTodoEffect = createEffect(
  (actions$ = inject(Actions), todoApiService = inject(TodoApiService)) =>
    actions$.pipe(
      ofType(TodoListActions.addTodo),
      switchMap(action => {
        return todoApiService.postTodo(action.todo)
          .pipe(
            map((todo) => TodoListActions.addTodoSuccess({todo})),
            catchError(err => of(TodoListActions.addTodoFailed({errorMessage: 'Wystąpił błąd.'})))
          )
      })
    ),
  {functional: true}
)
