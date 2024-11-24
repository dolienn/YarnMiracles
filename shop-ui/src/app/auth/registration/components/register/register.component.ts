import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DateUtilityService } from '../../../../date/services/date-utility/date-utility.service';
import { RegistrationDTO } from '../../models/registration-dto/registration-dto';
import { RegistrationService } from '../../services/registration/registration.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent implements OnInit {
  registrationDTO: RegistrationDTO = this.initializeRegistrationDTO();
  errorMsg: string[] = [];
  maxDate: string = '';

  private readonly maxAgeForOnlinePurchases: number = 13;

  constructor(
    private router: Router,
    private registrationService: RegistrationService,
    private dateUtilityService: DateUtilityService
  ) {}

  ngOnInit(): void {
    this.setMaxDateForDOB();
  }

  register(): void {
    this.clearErrors();
    this.formatDateOfBirth();

    this.registrationService.register(this.registrationDTO).subscribe({
      next: () => this.handleSuccessfulRegistration(),
      error: (error) => this.handleRegistrationError(error),
    });
  }

  navigateToLogin(): void {
    this.router.navigate(['login']);
  }

  private initializeRegistrationDTO(): RegistrationDTO {
    return {
      email: '',
      firstname: '',
      lastname: '',
      dateOfBirth: null,
      password: '',
    };
  }

  private setMaxDateForDOB(): void {
    const today = new Date();
    this.maxDate = this.dateUtilityService.calculateDateISO(
      today,
      this.maxAgeForOnlinePurchases
    );
  }

  private clearErrors(): void {
    this.errorMsg = [];
  }

  private formatDateOfBirth(): void {
    if (this.registrationDTO.dateOfBirth) {
      this.registrationDTO.dateOfBirth = new Date(
        this.registrationDTO.dateOfBirth as unknown as string
      );
    }
  }

  private handleSuccessfulRegistration(): void {
    this.router.navigate(['activate-account']);
  }

  private handleRegistrationError({ error }: any): void {
    this.errorMsg = error?.validationErrors || [
      error?.error || 'An unexpected error occurred',
    ];
  }
}
