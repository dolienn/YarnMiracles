import { Component, OnDestroy } from '@angular/core';
import { NotificationService } from '../../services/notification/notification.service';
import { Subject, Subscription, takeUntil } from 'rxjs';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss',
})
export class NotificationComponent implements OnDestroy {
  message: string = '';
  isSuccess: boolean = true;
  isVisible: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(private notificationService: NotificationService) {
    this.subscribeToNotifications();
    this.subscribeToNotificationType();
  }

  private subscribeToNotifications(): void {
    this.notificationService.message$
      .pipe(takeUntil(this.destroy$))
      .subscribe((message: string) => {
        this.message = message;
        this.isVisible = !!message;
      });
  }

  private subscribeToNotificationType(): void {
    this.notificationService.type$
      .pipe(takeUntil(this.destroy$))
      .subscribe((isSuccess: boolean) => {
        this.isSuccess = isSuccess;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
