import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../commons/authentication-request';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication-service.service';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  authRequest: AuthenticationRequest = { email: '', password: '' };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {}

  login() {
    this.errorMsg = [];
    this.authService.authenticate(this.authRequest).subscribe({
      next: (res) => {
        this.tokenService.token = res.token as string;
        this.router.navigate(['']);
      },
      error: (err) => {
        console.log(err);
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.error);
        }
      },
    });
  }
}
