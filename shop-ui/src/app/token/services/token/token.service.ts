import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { NotificationService } from '../../../notification/services/notification/notification.service';
import { User } from '../../../user/models/user/user';
import { environment } from '../../../../environments/environment.development';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private readonly jwtHelper = new JwtHelperService();
  private loggedIn = new BehaviorSubject<boolean>(this.isTokenValid());

  constructor(
    private notificationService: NotificationService,
    private httpClient: HttpClient,
    private router: Router
  ) {}

  set token(token: string) {
    localStorage.setItem('token', token);
    this.updateLoginStatus(this.isTokenValid());
  }

  get token() {
    return localStorage.getItem('token') ?? '';
  }

  storeToken(token: string): void {
    this.token = token;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  isTokenValid() {
    const token = this.token;
    if (!token) return false;

    if (this.jwtHelper.isTokenExpired(token)) {
      this.clearLocalStorage();
      return false;
    }

    return true;
  }

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  logout() {
    this.clearLocalStorage();
    this.updateLoginStatus(false);
    localStorage.setItem('logoutMessage', 'Successfully logged out');
    this.router.navigate(['']);
    window.location.reload();
  }

  getUserByJwtToken() {
    const jwtToken = this.token;
    if (!jwtToken) return null;

    return this.httpClient.get<User>(`${environment.url}/users/byAuth`, {
      headers: { Authorization: `Bearer ${jwtToken}` },
    });
  }

  private clearLocalStorage(): void {
    localStorage.clear();
  }

  private updateLoginStatus(isLoggedIn: boolean = true): void {
    this.loggedIn.next(isLoggedIn);
  }
}
