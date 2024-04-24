import { User } from "../../core/models/auth.model";
import { Action, createReducer, on } from "@ngrx/store";
import * as AuthActions from './auth.actions';

export interface AuthState {
  user: User | null;
  loading: boolean;
  error: string | null;
}

const initialState: AuthState = {
  user: null,
  loading: false,
  error: null,
}

const _authReducer = createReducer(
  initialState,
  on(AuthActions.login, (state, action) => ({
    ...state,//kopiujemy wszystkie własciwości + dodajemy loading ustawione na true
    loading: true,
  })),
  on(AuthActions.loginSuccess, (state, action) => ({
    ...state,
    loading: false,
    user: action.user,
    error: null,
  })),
  on(AuthActions.loginFailure, (state, action) => ({
    ...state,
    loading: false,
    error: action.error,
  })),
  on(AuthActions.register, (state, action) => ({
    ...state,
    loading: true,
  })),
  on(AuthActions.registerSuccess, (state, action) => ({
    ...state,
    loading: false,
    error: null,
  })),
  on(AuthActions.registerFailure, (state, action) => ({
    ...state,
    loading: false,
    error: action.error,
  })),
  on(AuthActions.clearError, (state, action) => ({
    ...state,
    error: null,
  }))

)

export function authReducer(state: AuthState | undefined, action: Action) {
  return _authReducer(state, action);
}
