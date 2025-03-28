import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { Credentials } from '../models/Credentials';
import { L } from '@angular/cdk/keycodes';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private authenticationServiceBaseURL: string = 'http://localhost:8080';
  public isAuthenticated = signal<boolean>(false);
  public token = signal<string>('');

  public authenticate(credentials: Credentials | undefined): Observable<any> {
    const basicAuth = credentials
      ? 'Basic ' + btoa(credentials.username + ':' + credentials.password)
      : '';

    const headers = new HttpHeaders({ authorization: basicAuth });

    return this.http
      .post(this.authenticationServiceBaseURL + '/login', null, {
        headers: headers,
      })
      .pipe(
        map((response) => {
          if ((response as { name: string }).name === credentials?.username) {
            this.isAuthenticated.set(true);
            this.token.set(basicAuth);
            localStorage.setItem('token', basicAuth);
          }
          return response;
        })
      );
  }

  public logout() {
    // this.http
    //   .post('logout', {})
    //   .pipe(
    //     finalize(() => {
    this.isAuthenticated.set(false);
    this.token.set('');
    this.router.navigateByUrl('/login', { state: { logout: true } });
    // })
    //   )
    //   .subscribe();
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
