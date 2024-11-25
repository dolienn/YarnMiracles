import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ActivationService } from '../services/activation/activation.service';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss',
})
export class ActivateAccountComponent {
  message: string = '';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(
    private router: Router,
    private activationService: ActivationService
  ) {}

  onCodeCompleted(token: string) {
    this.activateUser(token);
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  activateUser(token: string): void {
    this.activationService.activateUser(token).subscribe(
      () => {
        this.message =
          'Your account has been successfully activated. Now you can proceed to login.';
        this.submitted = true;
        this.isOkay = true;
      },
      () => {
        this.message = 'Token has been expired or is invalid.';
        this.submitted = true;
        this.isOkay = false;
      }
    );
  }
}
