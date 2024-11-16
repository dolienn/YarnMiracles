import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContactRequest } from '../../models/contact-request/contact-request';
import { ContactService } from '../../services/contact/contact.service';
import { TokenService } from '../../../token/services/token/token.service';
import { NotificationService } from '../../../notification/services/notification/notification.service';
import { User } from '../../../user/models/user/user';

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
    private tokenService: TokenService,
    private notificationService: NotificationService
  ) {}

  errorMsg: Array<string> = [];

  isLoading: boolean = false;

  sendLoading: boolean = false;

  user: User = new User();

  ngOnInit(): void {
    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user = data;
      this.contactRequest.email = this.user.email;
      this.isLoading = false;
    });
  }

  sendMessage() {
    console.log(this.contactRequest);
    this.sendLoading = true;
    this.errorMsg = [];
    this.contactService.sendMessage(this.contactRequest).subscribe({
      next: () => {
        this.notificationService.showMessage('Pomyślnie przesłano wiadomość');
        if (this.user === null) {
          this.contactRequest.email = '';
        } else {
          this.contactRequest.email = this.user.email;
        }
        this.contactRequest.subject = '';
        this.contactRequest.message = '';
        this.sendLoading = false;
      },
      error: (err) => {
        this.errorMsg = err.error.validationErrors;
        this.sendLoading = false;
      },
    });
  }
}
