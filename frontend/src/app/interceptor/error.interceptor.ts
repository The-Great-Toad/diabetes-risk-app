import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandlerFn,
  HttpRequest,
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { CustomErrorResponse } from '../models/CustomErrorResponse';

export function errorInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      console.log('Error from request ', req.url, ', response:', error);
      const customErrorResponse: CustomErrorResponse = {
        status: error.status,
        statusText: error.statusText,
        message: error.error?.message ?? error.message ?? 'Unknown error',
        url: req.url,
      };
      return throwError(() => customErrorResponse);
    })
  );
}
