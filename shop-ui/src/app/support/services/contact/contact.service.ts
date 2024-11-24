import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment.development';
import { SupportMessageDTO } from '../../models/support-message-dto/support-message-dto';

@Injectable({
  providedIn: 'root',
})
export class ContactService {
  private readonly contactUrl = `${environment.url}/support/send-message`;

  constructor(private httpClient: HttpClient) {}

  sendMessage(supportMessage: SupportMessageDTO): Observable<any> {
    return this.httpClient.post<SupportMessageDTO>(
      this.contactUrl,
      supportMessage
    );
  }
}
