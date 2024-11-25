import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { RegistrationDTO } from '../../models/registration-dto/registration-dto';
import { Observable } from 'rxjs';
import { User } from '../../../../user/models/user/user';

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {
  private readonly registerUrl = `${environment.url}/auth/register`;

  constructor(private httpClient: HttpClient) {}

  register(registrationDTO: RegistrationDTO): Observable<User> {
    return this.httpClient.post<User>(this.registerUrl, registrationDTO);
  }
}
