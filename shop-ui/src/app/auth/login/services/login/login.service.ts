import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequestDTO } from '../../models/login-request-dto/login-request-dto';
import { LoginResponseDTO } from '../../models/login-response-dto/login-response-dto';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private readonly loginUrl = `${environment.url}/auth/login`;

  constructor(private httpClient: HttpClient) {}

  login(loginRequest: LoginRequestDTO): Observable<LoginResponseDTO> {
    return this.httpClient.post<LoginResponseDTO>(this.loginUrl, loginRequest);
  }
}
