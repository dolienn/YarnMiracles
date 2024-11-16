import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationRequest } from '../../models/registration-request/registration-request';
import { AuthenticationService } from '../../services/authentication/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent implements OnInit {
  registerRequest: RegistrationRequest = {
    email: '',
    firstname: '',
    lastname: '',
    dateOfBirth: null,
    password: '',
  };
  errorMsg: Array<string> = [];

  maxDate: string = '';

  today: Date = new Date();
  maxAgeForOnlinePurchases: number = 13;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.maxDate = this.authService.calculateDate(
      this.today,
      this.maxAgeForOnlinePurchases
    );
  }

  register() {
    this.errorMsg = [];

    if (this.registerRequest.dateOfBirth != null) {
      const formattedDate = new Date(this.registerRequest.dateOfBirth as any);
      this.registerRequest.dateOfBirth = formattedDate;
    }

    this.authService.register(this.registerRequest).subscribe({
      next: () => {
        this.router.navigate(['activate-account']);
      },
      error: (err) => {
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else if (err.error.error) {
          this.errorMsg = [err.error.error];
        }
      },
    });
  }

  login() {
    this.router.navigate(['login']);
  }
}
