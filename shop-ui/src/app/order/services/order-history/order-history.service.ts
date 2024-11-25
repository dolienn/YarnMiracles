import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderHistory } from '../../models/order-history/order-history';
import { environment } from '../../../../environments/environment.development';
import { Page } from '../../../shared/models/page/page';
import { OrderHistoryResponse } from '../../models/order-history-response/order-history-response';
import { PaginationParams } from '../../../pagination/models/pagination-params/pagination-params';

@Injectable({
  providedIn: 'root',
})
export class OrderHistoryService {
  private orderUrl = `${environment.url}/orders`;

  constructor(private httpClient: HttpClient) {}

  getOrderHistory(
    email: string,
    paginationParams: PaginationParams
  ): Observable<OrderHistoryResponse> {
    const orderHistoryUrl = `${this.orderUrl}/search/findByCustomerEmailOrderByDateCreatedDesc?email=${email}&page=${paginationParams.page}&size=${paginationParams.size}`;

    return this.httpClient.get<OrderHistoryResponse>(orderHistoryUrl);
  }
}
