import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../../../user/models/user/user';
import { TokenService } from '../../../../token/services/token/token.service';
import { PasswordRequestDTO } from '../../../password/models/password-request-dto/password-request-dto';
import { RegistrationDTO } from '../../../registration/models/registration-dto/registration-dto';
import { UserProfileService } from '../../services/user-profile/user-profile.service';
import { UserProfileDTO } from '../../models/user-profile-dto/user-profile-dto';
import { PasswordService } from '../../../password/services/password/password.service';
import { DateUtilityService } from '../../../../date/services/date-utility/date-utility.service';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.scss',
})
export class UserDetailsComponent implements OnInit {
  userProfileDTO: UserProfileDTO = this.initializeUserProfileDTO();
  passwordRequest: PasswordRequestDTO = this.initializePasswordRequestDTO();

  errorMsg: string[] = [];
  errorMsgPassword: string[] = [];
  isLoading = true;
  user: User = new User();
  maxDate = '';

  private readonly today: Date = new Date();
  private readonly maxAgeForOnlinePurchases = 13;

  constructor(
    private tokenService: TokenService,
    private dateUtilityService: DateUtilityService,
    private userProfileService: UserProfileService,
    private passwordService: PasswordService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.setMaxDateForDOB();
    this.loadUserDetails();
  }

  changeAccountDetails(): void {
    this.clearErrors();

    if (this.userProfileDTO.dateOfBirth) {
      this.userProfileDTO.dateOfBirth = new Date(
        this.userProfileDTO.dateOfBirth as any
      );
    }

    this.userProfileService.updateUserProfile(this.userProfileDTO).subscribe({
      next: () => {
        this.handleEmailChange(this.user.email, this.userProfileDTO.email);
        this.router.navigate(['']);
      },
      error: (err) => this.handleErrors(err, true),
    });
  }

  changePassword(): void {
    this.clearPasswordErrors();

    this.passwordService.changePassword(this.passwordRequest).subscribe({
      next: () => this.router.navigate(['']),
      error: (err) => this.handleErrors(err, false),
    });
  }

  private initializeUserProfileDTO(): UserProfileDTO {
    return {
      email: '',
      firstname: '',
      lastname: '',
      dateOfBirth: null,
      password: '',
    };
  }

  private initializePasswordRequestDTO(): PasswordRequestDTO {
    return {
      currentPassword: '',
      newPassword: '',
    };
  }

  private setMaxDateForDOB(): void {
    this.maxDate = this.dateUtilityService.calculateFormattedDate(
      this.today,
      this.maxAgeForOnlinePurchases
    );
  }

  private loadUserDetails(): void {
    this.tokenService.getUserByJwtToken()?.subscribe({
      next: (data) => {
        this.user = data;
        this.populateUserProfile();
        this.isLoading = false;

        if (this.user.id === 0) {
          this.router.navigate(['login']);
        }
      },
    });
  }

  private populateUserProfile(): void {
    const { firstname, lastname, email, dateOfBirth } = this.user;
    Object.assign(this.userProfileDTO, {
      firstname,
      lastname,
      email,
      dateOfBirth,
    });
  }

  private clearErrors(): void {
    this.errorMsg = [];
  }

  private handleEmailChange(oldEmail: string, newEmail: string) {
    if (oldEmail !== newEmail) {
      this.tokenService.logout();
    }
  }

  private handleErrors(err: any, isAccountError: boolean): void {
    const errorList = err.error?.validationErrors || [
      err.error?.error || 'An unexpected error occurred',
    ];

    if (isAccountError) {
      this.errorMsg = errorList;
    } else {
      this.errorMsgPassword = errorList;
    }
  }

  private clearPasswordErrors(): void {
    this.errorMsgPassword = [];
  }
}
