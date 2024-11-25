import { Page } from '../../../shared/models/page/page';
import { OrderHistory } from '../order-history/order-history';

export interface OrderHistoryResponse {
  _embedded: {
    orders: OrderHistory[];
  };
  page: Page;
}
