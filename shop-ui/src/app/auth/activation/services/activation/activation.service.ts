import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment.development';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ActivationService {
  private readonly activateUrl = `${environment.url}/auth/activate`;

  constructor(private httpClient: HttpClient) {}

  activateUser(activationToken: string): Observable<void> {
    const params = new HttpParams().set('token', activationToken);
    return this.httpClient.get<void>(this.activateUrl, { params });
  }
}
