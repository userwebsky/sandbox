import { createAction, props } from "@ngrx/store";
import { Todo } from "../../shared/interfaces/todo.interface";

// export const addTodo = createAction(
//   '[Todo List] Add Todo',
//   props<{todo: Todo}>()
// );

export const deleteTodo = createAction(
  '[Todo List] Delete Todo',
  props<{ id: number }>()
)

export const changeTodoStatus = createAction(
  '[Todo List] Change Status Todo',
  props<{ id: number }>()
)

export const fetchTodos = createAction(
  '[Todo List] Fetch Todos'
)

export const fetchTodosSuccess = createAction(
  '[Todo List] Fetch Todos Success',
  props<{ todos: Todo[] }>()
)

export const fetchTodosFailed = createAction(
  '[Todo List] Fetch Todos Failed',
  props<{ errorMessage: string }>()
)

export const addTodo = createAction(
  '[Todo List] Add Todo',
  props<{ todo: Omit<Todo, "id"> }>()
)

export const addTodoSuccess = createAction(
  '[Todo List] Add Todo Success',
  props<{ todo: Todo }>()
)

export const addTodoFailed = createAction(
  '[Todo List] Add Todo Failed',
  props<{ errorMessage: string }>()
)
