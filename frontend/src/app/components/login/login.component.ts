import { Component, inject, signal } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

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

  credentials = { username: '', password: '' };

  constructor() {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state && 'logout' in navigation.extras.state) {
      this.hasLogout.set(navigation.extras.state['logout']);
    }
  }

  public login(): void {
    // this.authenticationService.authenticate(this.credentials, () => {
    //   this.router.navigateByUrl('/patients');
    // });
    this.authenticationService.authenticate(this.credentials).subscribe({
      next: () => {
        this.router.navigateByUrl('/patients');
      },
      error: (error: HttpErrorResponse) => {
        console.log('error', error);
        this.error.set(error.message);
      },
    });
  }
}
