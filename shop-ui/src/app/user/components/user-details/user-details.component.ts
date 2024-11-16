import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationRequest } from '../../../auth/models/registration-request/registration-request';
import { PasswordRequest } from '../../../auth/models/password-request/password-request';
import { User } from '../../models/user/user';
import { TokenService } from '../../../token/services/token/token.service';
import { AuthenticationService } from '../../../auth/services/authentication/authentication.service';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.scss',
})
export class UserDetailsComponent implements OnInit {
  registerRequest: RegistrationRequest = {
    email: '',
    firstname: '',
    lastname: '',
    dateOfBirth: null,
    password: '',
  };

  passwordRequest: PasswordRequest = {
    yourPassword: '',
    newPassword: '',
  };

  errorMsg: Array<string> = [];

  errorMsgPassword: Array<string> = [];

  isLoading: boolean = true;

  user: User = new User();

  maxDate: string = '';

  today: Date = new Date();
  maxAgeForOnlinePurchases: number = 13;

  constructor(
    private tokenService: TokenService,
    private authService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.maxDate = this.authService.calculateDate(
      this.today,
      this.maxAgeForOnlinePurchases
    );

    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user = data;
      this.isLoading = false;

      if (this.user.id == 0) {
        this.router.navigate(['login']);
      }
    });
  }

  changeAccountDetails() {
    this.errorMsg = [];

    if (this.registerRequest.dateOfBirth != null) {
      const formattedDate = new Date(this.registerRequest.dateOfBirth as any);
      this.registerRequest.dateOfBirth = formattedDate;
    }

    this.authService.changeAccountDetails(this.registerRequest).subscribe({
      next: () => {
        this.router.navigate(['']);
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

  changePassword() {
    this.errorMsgPassword = [];
    this.authService.changePassword(this.passwordRequest).subscribe({
      next: () => {
        this.router.navigate(['']);
      },
      error: (err) => {
        if (err.error.validationErrors) {
          this.errorMsgPassword = err.error.validationErrors;
        } else if (err.error.error) {
          this.errorMsgPassword = [err.error.error];
        }
      },
    });
  }
}
