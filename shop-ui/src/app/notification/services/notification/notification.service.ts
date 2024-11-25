import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private readonly MESSAGE_DISPLAY_DURATION = 3000; // Duration in milliseconds

  private messageSource = new BehaviorSubject<string>('');
  message$ = this.messageSource.asObservable();

  private isVisibleSource = new BehaviorSubject<boolean>(false);
  isVisible$ = this.isVisibleSource.asObservable();

  private typeSource = new BehaviorSubject<boolean>(true);
  type$ = this.typeSource.asObservable();

  showMessage(
    message: string,
    isSuccess: boolean,
    duration: number = this.MESSAGE_DISPLAY_DURATION
  ): void {
    this.messageSource.next(message);
    this.isVisibleSource.next(true);
    this.typeSource.next(isSuccess);
    this.autoHideMessage(duration);
  }

  private autoHideMessage(duration: number): void {
    setTimeout(() => {
      this.hideMessage();
    }, duration);
  }

  private hideMessage(): void {
    this.messageSource.next('');
    this.isVisibleSource.next(false);
    this.typeSource.next(true);
  }
}
