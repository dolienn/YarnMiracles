import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { User } from '../../../user/models/user/user';
import { SummaryMetrics } from '../../models/summary-metrics/summary-metrics';
import { UserRequestDTO } from '../../../user/models/user-request-dto/user-request-dto';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private httpClient: HttpClient) {}

  getSummaryMetrics(): Observable<SummaryMetrics> {
    const url = `${environment.url}/summary-metrics`;

    return this.httpClient.get<SummaryMetrics>(url);
  }
}
