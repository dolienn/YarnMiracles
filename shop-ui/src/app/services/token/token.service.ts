import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BehaviorSubject } from 'rxjs';
import { NotificationService } from '../notification/notification.service';
import { HttpClient } from '@angular/common/http';
import { User } from '../../common/user/user';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  constructor(
    private notificationService: NotificationService,
    private httpClient: HttpClient
  ) {}

  private loggedIn = new BehaviorSubject<boolean>(this.isTokenValid());

  set token(token: string) {
    localStorage.setItem('token', token);
    this.loggedIn.next(this.isTokenValid());
    this.notificationService.showMessage('Pomyślnie zalogowano');
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }

    const jwtHelper = new JwtHelperService();
    const isTokenExpired = jwtHelper.isTokenExpired(token);

    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }

    return true;
  }

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  logout() {
    localStorage.clear();
    this.loggedIn.next(false);
    this.notificationService.showMessage('Pomyślnie wylogowano');
  }

  getUserInfo() {
    const token = localStorage.getItem('token');
    if (token) {
      return this.httpClient.get<User>(
        'http://localhost:8088/api/v1/auth/info',
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
    }

    return null;
  }
}
