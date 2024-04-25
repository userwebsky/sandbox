import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable()
export class ErrorHandlingInterceptor implements HttpInterceptor {
  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMsg = '';

        if (error.status >= 400 && error.status < 500) {
          errorMsg = error.error.message || 'Wystąpił błąd. Spróbuj ponownie.';
        } else {
          errorMsg = 'Wystąpił błąd. Spróbuj ponownie.';
        }

        return throwError(errorMsg);
      })
    );
  }
}
