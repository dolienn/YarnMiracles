import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Feedback } from '../../common/feedback/feedback';
import { Observable } from 'rxjs';
import { FeedbackRequest } from '../../common/feedback-request/feedback-request';

@Injectable({
  providedIn: 'root',
})
export class FeedbackService {
  private readonly feedbackUrl = 'http://localhost:8088/api/v1/feedbacks';

  constructor(private httpClient: HttpClient) {}

  getFeedbacksByProduct(
    page: number,
    pageSize: number,
    productId: number
  ): Observable<GetResponseFeedbacks> {
    const feedbacksByProductUrl = `${this.feedbackUrl}/product/${productId}?page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseFeedbacks>(feedbacksByProductUrl);
  }

  saveFeedback(request: FeedbackRequest): Observable<number> {
    return this.httpClient.post<number>(this.feedbackUrl, request);
  }
}

interface GetResponseFeedbacks {
  content: Feedback[];
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}
