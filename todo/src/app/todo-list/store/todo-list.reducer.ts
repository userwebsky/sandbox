import { Todo } from "../../shared/interfaces/todo.interface";
import { Action, createReducer, on } from "@ngrx/store";
import * as TodoListActions from './todo-list.action'

export interface TodoListState {
  todos: Todo[],
  fetchTodosErrorMessage: string | null,
  loading: boolean,

  addTodoErrorMessage: string | null
}

const initialState: TodoListState = {//początkowy stan listy zadań
  todos: [
    // {
    //   id: 1,
    //   isComplete: true,
    //   name: 'Umyj naczynia.'
    // },
    // {
    //   id: 2,
    //   isComplete: true,
    //   name: 'Umyj naczynia2.'
    // },
    // {
    //   id: 3,
    //   isComplete: false,
    //   name: 'Umyj naczynia3.'
    // },
  ],
  fetchTodosErrorMessage: null,
  loading: false,
  addTodoErrorMessage: null
}

const _todoListReducer = createReducer(
  initialState,
  // on(
  //   TodoListActions.addTodo,
  //   (state, action) => ({
  //     ...state,
  //     todos: state.todos.concat({...action.todo})
  //   })
  // ),
  on(
    TodoListActions.deleteTodo,
    (state, action) => ({
      ...state,
      todos: state.todos.filter(todo => todo.id !== action.id)
    })
  ),
  on(
    TodoListActions.changeTodoStatus,
    (state, action) => ({
      ...state,
      todos: state.todos.map(todo => todo.id === action.id ? {...todo, isComplete: !todo.isComplete} : todo)
    })
  ),
  on(
    TodoListActions.fetchTodosSuccess,
    (state, action) => ({
      ...state,
      todos: [...action.todos],
      loading: false,
      fetchTodosErrorMessage: null
    })
  ),
  on(
    TodoListActions.fetchTodos,
    (state, action) => ({
      ...state,
      loading: true
    })
  ),
  on(
    TodoListActions.fetchTodosFailed,
    (state, action) => ({
      ...state,
      loading: false,
      fetchTodosErrorMessage: action.errorMessage
    })
  ),
  on(
    TodoListActions.addTodoSuccess,
    (state, action) => ({
      ...state,
      todos: state.todos.concat({...action.todo}),
      loading: false,
      addTodoErrorMessage: null
    })
  ),
  on(
    TodoListActions.addTodo,
    (state, action) => ({
      ...state,
      loading: true
    })
  ),
  on(
    TodoListActions.addTodoFailed,
    (state, action) => ({
      ...state,
      loading: false,
      fetchTodosErrorMessage: action.errorMessage
    })
  )
)

export function todoListReducer(state: TodoListState | undefined, action: Action) {
  return _todoListReducer(state, action);
}
