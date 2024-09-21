import { Component, OnInit } from '@angular/core';
import { RegistrationRequest } from '../../common/registration-request/registration-request';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { Router } from '@angular/router';

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
    this.maxDate = this.calculateDate(
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
        this.errorMsg = err.error.validationErrors;
      },
    });
  }

  login() {
    this.router.navigate(['login']);
  }

  calculateDate(date: Date, yearAgo: number) {
    const year = date.getFullYear() - yearAgo;
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);

    return `${year}-${month}-${day}`;
  }
}
