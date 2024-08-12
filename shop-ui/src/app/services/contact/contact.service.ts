import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { ContactRequest } from '../../common/contact-request/contact-request';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { NotificationService } from '../notification/notification.service';

@Injectable({
  providedIn: 'root',
})
export class ContactService {
  private readonly contactUrl = `${environment.url}/contact`;

  constructor(private httpClient: HttpClient) {}

  sendMessage(contactRequest: ContactRequest): Observable<any> {
    return this.httpClient.post<ContactRequest>(
      this.contactUrl,
      contactRequest
    );
  }
}
