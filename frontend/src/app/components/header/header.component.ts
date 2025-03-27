import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  private authenticationService = inject(AuthService);

  public isAuthenticated = this.authenticationService.isAuthenticated;

  public logout() {
    this.authenticationService.logout();
  }
}
