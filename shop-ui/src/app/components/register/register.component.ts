import { Component } from '@angular/core';
import { RegistrationRequest } from '../../commons/registration-request';
import { AuthenticationService } from '../../services/authentication-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  registerRequest: RegistrationRequest = {
    email: '',
    firstname: '',
    lastname: '',
    password: '',
  };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  register() {
    this.errorMsg = [];
    this.authService.register(this.registerRequest).subscribe({
      next: () => {
        this.router.navigate(['activate-account']);
      },
      error: (err) => {
        this.errorMsg = err.error.validationErrors;
      },
    });
  }

  login() {
    this.router.navigate(['login']);
  }
}
