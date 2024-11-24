import { PaginationParams } from '../../../pagination/models/pagination-params/pagination-params';
import { Page } from '../../../shared/models/page/page';
import { Feedback } from '../feedback/feedback';

export interface FeedbackResponse {
  content: Feedback[];
  page: Page;
}
