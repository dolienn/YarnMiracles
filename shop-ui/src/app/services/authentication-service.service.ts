import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationRequest } from '../commons/authentication-request';
import { Observable } from 'rxjs';
import { RegistrationRequest } from '../commons/registration-request';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private readonly authenticateUrl =
    'http://localhost:8088/api/v1/auth/authenticate';

  private readonly registerUrl = 'http://localhost:8088/api/v1/auth/register';

  private readonly confirmUrl =
    'http://localhost:8088/api/v1/auth/activate-account';

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
