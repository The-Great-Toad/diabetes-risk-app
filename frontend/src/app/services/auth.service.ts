import { HttpClient, HttpResponse } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { LoginRequest } from '../models/LoginRequest';
import { LoginResponse } from '../models/LoginResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private authServiceBaseUrl: string = 'http://localhost:8080';
  private authServiceLoginUri: string = '/auth/login';
  public isAuthenticated = signal<boolean>(false);
  public token = signal<string>('');

  public authenticate(credentials: LoginRequest): Observable<any> {
    return this.http
      .post(this.authServiceBaseUrl + this.authServiceLoginUri, credentials)
      .pipe(
        map((response) => {
          if ((response as LoginResponse).token) {
            const authToken = (response as LoginResponse).token;
            this.isAuthenticated.set(true);
            this.token.set(authToken);
            localStorage.setItem('token', authToken);
          }

          return response;
        })
      );
  }

  public logout() {
    this.isAuthenticated.set(false);
    this.token.set('');
    localStorage.removeItem('token');
    this.router.navigateByUrl('/login', { state: { logout: true } });
  }

  private getToken(): string {
    const token = localStorage.getItem('token');
    return token ? token : '';
  }

  public isLoggedIn(): boolean {
    const token = this.getToken();
    if (token) {
      this.isAuthenticated.set(true);
      this.token.set(token);
    }

    return this.isAuthenticated() && this.token() !== '';
  }
}
