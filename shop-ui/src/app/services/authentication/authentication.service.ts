import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationRequest } from '../../common/authentication-request/authentication-request';
import { Observable } from 'rxjs';
import { RegistrationRequest } from '../../common/registration-request/registration-request';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private readonly authenticateUrl = `${environment.url}/auth/authenticate`;

  private readonly registerUrl = `${environment.url}/auth/register`;

  private readonly confirmUrl = `${environment.url}/activate-account`;

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

  confirm(token: string): Observable<void> {
    const params = new HttpParams().set('token', token);
    return this.httpClient.get<void>(this.confirmUrl, { params });
  }
}
