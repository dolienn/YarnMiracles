import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private messageSource = new BehaviorSubject<string>('');
  message$ = this.messageSource.asObservable();

  private isVisibleSource = new BehaviorSubject<boolean>(false);
  isVisible$ = this.isVisibleSource.asObservable();

  showMessage(message: string) {
    this.messageSource.next(message);
    this.isVisibleSource.next(true);
    setTimeout(() => {
      this.messageSource.next('');
      this.isVisibleSource.next(false);
    }, 3000);
  }

  clearMessage() {
    this.messageSource.next('');
    this.isVisibleSource.next(false);
  }
}
