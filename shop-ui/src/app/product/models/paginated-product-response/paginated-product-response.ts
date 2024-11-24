import { Page } from '../../../shared/models/page/page';
import { Product } from '../product/product';

export interface PaginatedProductResponse {
  content: Product[];
  page: Page;
}
