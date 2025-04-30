import { HttpHeaders, HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authenticationService = inject(AuthService);
  const token = authenticationService.token();
  //   console.log('auth interceptor - token', token);

  if (!token) {
    return next(req);
  }

  const headers = new HttpHeaders({ Authorization: 'Bearer ' + token });
  console.log('auth interceptor - headers', headers);

  const newReq = req.clone({ headers });
  console.log('auth interceptor - newReq', newReq);

  return next(newReq);
};
