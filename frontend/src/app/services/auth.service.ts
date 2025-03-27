import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { Credentials } from '../models/Credentials';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private authenticationServiceBaseURL: string = 'http://localhost:8080';
  public isAuthenticated = signal<boolean>(false);
  public username = signal<string>('');
  public token = signal<string>('');

  public getUsername(): string {
    return this.username();
  }

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
}
