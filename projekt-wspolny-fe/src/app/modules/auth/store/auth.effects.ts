import { Injectable } from '@angular/core';
import { Actions } from '@ngrx/effects';
import { AuthService } from '../../core/services/auth.service';

@Injectable()
export class AuthEffects {
  constructor(private actions$: Actions, private authService: AuthService) {}
}
