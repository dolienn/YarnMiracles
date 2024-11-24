import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../../../token/services/token/token.service';
import { LoginRequestDTO } from '../../models/login-request-dto/login-request-dto';
import { LoginService } from '../../services/login/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  loginRequest: LoginRequestDTO = { email: '', password: '' };
  errorMsg: string[] = [];

  constructor(
    private router: Router,
    private loginService: LoginService,
    private tokenService: TokenService
  ) {}

  login(): void {
    this.clearErrors();

    this.loginService.login(this.loginRequest).subscribe({
      next: (response) => {
        this.handleSuccessfulLogin(response.token);
      },
      error: (error) => this.handleLoginError(error),
    });
  }

  private handleSuccessfulLogin(token: string): void {
    this.tokenService.storeToken(token);
    this.router.navigate(['']);
    localStorage.setItem('loginMessage', 'Successfully logged in');
    window.location.reload();
  }

  private handleLoginError(error: any): void {
    const validationErrors = error.error?.validationErrors;
    const generalError = error.error?.error;

    this.errorMsg = validationErrors || [
      generalError || 'An unexpected error occurred',
    ];
  }

  private clearErrors(): void {
    this.errorMsg = [];
  }
}
