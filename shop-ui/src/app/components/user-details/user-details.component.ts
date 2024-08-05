import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../services/token/token.service';
import { User } from '../../common/user/user';
import { Router } from '@angular/router';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { FormValidators } from '../../validators/form-validators';
import { RegistrationRequest } from '../../common/registration-request/registration-request';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { PasswordRequest } from '../../common/password-request/password-request';

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

  constructor(
    private tokenService: TokenService,
    private authService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
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
