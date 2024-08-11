import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationRequest } from '../../common/authentication-request/authentication-request';
import { Observable } from 'rxjs';
import { RegistrationRequest } from '../../common/registration-request/registration-request';
import { environment } from '../../../environments/environment.development';
import { PasswordRequest } from '../../common/password-request/password-request';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private readonly authenticateUrl = `${environment.url}/auth/authenticate`;

  private readonly registerUrl = `${environment.url}/auth/register`;

  private readonly confirmUrl = `${environment.url}/auth/activate-account`;

  private readonly changeAccDetailsUrl = `${environment.url}/auth/change-account-details`;

  private readonly changePasswordUrl = `${environment.url}/auth/change-password`;

  constructor(private httpClient: HttpClient) {}

  authenticate(authRequest: AuthenticationRequest): Observable<any> {
    return this.httpClient.post<AuthenticationRequest>(
      this.authenticateUrl,
      authRequest
    );
  }

  register(registerRequest: RegistrationRequest): Observable<any> {
    return this.httpClient.post<RegistrationRequest>(
      this.registerUrl,
      registerRequest
    );
  }

  changeAccountDetails(registerRequest: RegistrationRequest): Observable<any> {
    return this.httpClient.post<RegistrationRequest>(
      this.changeAccDetailsUrl,
      registerRequest
    );
  }

  changePassword(passwordRequest: PasswordRequest): Observable<any> {
    return this.httpClient.post<PasswordRequest>(
      this.changePasswordUrl,
      passwordRequest
    );
  }

  confirm(token: string): Observable<void> {
    const params = new HttpParams().set('token', token);
    return this.httpClient.get<void>(this.confirmUrl, { params });
  }
}
