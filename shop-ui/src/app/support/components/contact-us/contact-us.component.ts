import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContactService } from '../../services/contact/contact.service';
import { TokenService } from '../../../token/services/token/token.service';
import { NotificationService } from '../../../notification/services/notification/notification.service';
import { User } from '../../../user/models/user/user';
import { SupportMessageDTO } from '../../models/support-message-dto/support-message-dto';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrl: './contact-us.component.scss',
})
export class ContactUsComponent implements OnInit {
  supportMessage: SupportMessageDTO = this.initializeSupportMessage();
  errorMsg: string[] = [];
  isLoading: boolean = false;
  sendLoading: boolean = false;
  user: User = new User();

  constructor(
    private contactService: ContactService,
    private tokenService: TokenService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
  }

  sendMessage(): void {
    this.sendLoading = true;
    this.resetErrorMessages();

    this.contactService.sendMessage(this.supportMessage).subscribe({
      next: () => this.handleMessageSuccess(),
      error: (err) => this.handleMessageError(err),
    });
  }

  private initializeSupportMessage(): SupportMessageDTO {
    return {
      from: '',
      subject: '',
      text: '',
    };
  }

  private loadUserData(): void {
    this.tokenService.getUserByJwtToken()?.subscribe((data) => {
      this.user = data;
      this.supportMessage.from = this.user?.email || '';
      this.isLoading = false;
    });
  }

  private resetErrorMessages(): void {
    this.errorMsg = [];
  }

  private handleMessageSuccess(): void {
    this.resetContactRequest();
    this.sendLoading = false;
    this.notificationService.showMessage('Successfully sent the message', true);
  }

  private resetContactRequest(): void {
    this.supportMessage = {
      from: this.user?.email || '',
      subject: '',
      text: '',
    };
  }

  private handleMessageError(err: any): void {
    this.errorMsg = err.error.validationErrors || [err.error];
    this.sendLoading = false;
    this.notificationService.showMessage('Failed to send the message', false);
  }
}
