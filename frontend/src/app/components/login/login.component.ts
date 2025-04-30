import { Component, inject, signal } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { CustomErrorResponse } from '../../models/CustomErrorResponse';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private authenticationService = inject(AuthService);
  private router = inject(Router);

  //   public error = signal<HttpErrorResponse | null>(null);
  public error = signal<String>('');
  public hasLogout = signal<boolean>(false);
  public logoutSuccessMsg = signal<string>(
    'You have been logged out successfully.'
  );
  public invalidCredentialsMsg = signal<string>('');

  credentials = { username: '', password: '' };

  constructor() {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state && 'logout' in navigation.extras.state) {
      this.hasLogout.set(navigation.extras.state['logout']);
    }
  }

  public login(): void {
    this.hasLogout.set(false);
    this.invalidCredentialsMsg.set('');

    this.authenticationService.authenticate(this.credentials).subscribe({
      next: (res) => {
        console.log('login response: ', res);
        this.router.navigateByUrl('/patients');
      },
      error: (error: CustomErrorResponse) => {
        console.log('login error: ', error);
        if (error.status === 401 || error.status === 403) {
          this.invalidCredentialsMsg.set(
            'Invalid username or password. Please try again.'
          );
        } else {
          this.error.set(error.message);
        }
      },
    });
  }
}
