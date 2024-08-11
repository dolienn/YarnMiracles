import { Component, OnInit } from '@angular/core';
import { ContactRequest } from '../../common/contact-request/contact-request';
import { ContactService } from '../../services/contact/contact.service';
import { TokenService } from '../../services/token/token.service';
import { User } from '../../common/user/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrl: './contact-us.component.scss',
})
export class ContactUsComponent implements OnInit {
  contactRequest: ContactRequest = {
    email: '',
    subject: '',
    message: '',
  };

  constructor(
    private contactService: ContactService,
    private tokenService: TokenService
  ) {}

  errorMsg: Array<string> = [];

  isLoading: boolean = false;

  user: User = new User();

  ngOnInit(): void {
    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user = data;
      this.isLoading = false;
    });
  }

  sendMessage() {
    this.errorMsg = [];
    this.contactService.sendMessage(this.contactRequest).subscribe({
      next: () => {
        console.log('WORK');
      },
      error: (err) => {
        this.errorMsg = err.error.validationErrors;
      },
    });
  }
}
