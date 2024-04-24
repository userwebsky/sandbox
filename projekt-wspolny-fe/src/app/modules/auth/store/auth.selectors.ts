import { AppState } from '../../../store/app.reducer';
import { createSelector } from '@ngrx/store';
import { AuthState } from './auth.reducer';

export const selectAuth = (state: AppState) => state.auth;

export const selectAuthUser = createSelector(
  selectAuth,
  (state: AuthState) => state.user // zwracamy usera
);

export const selectAuthLoading = createSelector(
  selectAuth,
  (state: AuthState) => state.loading // zwracamy loading
);
export const selectAuthError = createSelector(
  selectAuth,
  (state: AuthState) => state.error //zwracamy error
);
