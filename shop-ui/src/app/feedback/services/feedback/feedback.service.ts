import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaginationParams } from '../../../pagination/models/pagination-params/pagination-params';
import { environment } from '../../../../environments/environment.development';
import { FeedbackResponse } from '../../models/feedback-response/feedback-response';
import { FeedbackRequest } from '../../models/feedback-request/feedback-request';

@Injectable({
  providedIn: 'root',
})
export class FeedbackService {
  private readonly feedbackUrl = `${environment.url}/feedbacks`;

  constructor(private httpClient: HttpClient) {}

  getFeedbacksByProduct(
    productId: number,
    paginationParams: PaginationParams
  ): Observable<FeedbackResponse> {
    const feedbacksByProductUrl = `${this.feedbackUrl}/products/${productId}?page=${paginationParams.page}&size=${paginationParams.size}`;

    return this.httpClient.get<FeedbackResponse>(feedbacksByProductUrl);
  }

  saveFeedback(request: FeedbackRequest): Observable<number> {
    return this.httpClient.post<number>(this.feedbackUrl, request);
  }
}
