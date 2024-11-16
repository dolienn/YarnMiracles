import { Component } from '@angular/core';
import { NotificationService } from '../../services/notification/notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss',
})
export class NotificationComponent {
  message: string = '';

  constructor(private notificationService: NotificationService) {
    this.notificationService.message$.subscribe((msg) => {
      this.message = msg;
      if (msg) {
        setTimeout(() => (this.message = ''), 3000); // Znika po 3 sekundach
      }
    });
  }
}
