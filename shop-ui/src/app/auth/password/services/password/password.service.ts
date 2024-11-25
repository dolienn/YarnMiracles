import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { PasswordRequestDTO } from '../../models/password-request-dto/password-request-dto';
import { Observable } from 'rxjs';
import { User } from '../../../../user/models/user/user';

@Injectable({
  providedIn: 'root',
})
export class PasswordService {
  private readonly changePasswordUrl = `${environment.url}/auth/change-password`;

  constructor(private httpClient: HttpClient) {}

  changePassword(passwordRequest: PasswordRequestDTO): Observable<User> {
    return this.httpClient.post<User>(this.changePasswordUrl, passwordRequest);
  }
}
