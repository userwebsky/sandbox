import { createAction, props } from '@ngrx/store';
import { IUser, LoginData, RegisterData } from "../../core/models/auth.model";

const LOGIN_TYPE = '[Auth] Login';
const LOGIN_SUCCESS_TYPE = '[Auth] Login Success';
const LOGIN_FAILURE_TYPE = '[Auth] Login Failure';

const REGISTER_TYPE = '[Auth] Register';
const REGISTER_SUCCESS_TYPE = '[Auth] Register Success';
const REGISTER_FAILURE_TYPE = '[Auth] Register Failure';

const CLEAR_ERROR_TYPE = '[Auth] Clear Error';

export const login = createAction(
  LOGIN_TYPE,
  props<{ loginData: LoginData }>()
);

export const loginSuccess = createAction(
  LOGIN_SUCCESS_TYPE,
  props<{ user: IUser }>()
);

export const loginFailure = createAction(
  LOGIN_FAILURE_TYPE,
  props<{ error: string }>()
);

export const register = createAction(
  REGISTER_TYPE,
  props<{ registerData: RegisterData }>()
);

export const registerSuccess = createAction(REGISTER_SUCCESS_TYPE);

export const registerFailure = createAction(
  REGISTER_FAILURE_TYPE,
  props<{ error: string }>()
);

export const clearError = createAction(CLEAR_ERROR_TYPE);
