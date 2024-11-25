import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { RatingChangeEvent } from 'angular-star-rating';
import { Feedback } from '../../models/feedback/feedback';
import { FeedbackRequest } from '../../models/feedback-request/feedback-request';
import { User } from '../../../user/models/user/user';
import { Product } from '../../../product/models/product/product';
import { UserService } from '../../../user/services/user/user.service';
import { TokenService } from '../../../token/services/token/token.service';
import { Page } from '../../../shared/models/page/page';
import { PaginationParams } from '../../../pagination/models/pagination-params/pagination-params';
import { FeedbackService } from '../../services/feedback/feedback.service';
import { FeedbackResponse } from '../../models/feedback-response/feedback-response';
import { NotificationService } from '../../../notification/services/notification/notification.service';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.scss',
})
export class FeedbackComponent implements OnInit {
  @ViewChild('feedbackSection') feedbackSection!: ElementRef;
  @Input() product!: Product;

  isLoadingComments: boolean = true;
  feedbacks: Feedback[] = [];
  isNotLoggedIn: boolean = true;
  isPurchasedProduct: boolean = false;

  feedback: FeedbackRequest = new FeedbackRequest();
  user: User = new User();
  paginationParams = new PaginationParams();

  page: Page = {
    number: 1,
    size: 10,
    totalElements: 0,
  };

  constructor(
    private feedbackService: FeedbackService,
    private userService: UserService,
    private tokenService: TokenService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.listFeedbacks();
    this.initializeUser();
  }

  hasAlreadySentFeedback(): boolean {
    return (
      !!this.user &&
      this.feedbacks.some((feedback) => feedback.createdBy?.id === this.user.id)
    );
  }

  onRatingChange(event: RatingChangeEvent): void {
    this.feedback.note = event.rating;
  }

  saveFeedback(): void {
    if (this.isFeedbackValid()) {
      this.feedback.productId = this.product.id;

      this.feedbackService.saveFeedback(this.feedback).subscribe({
        next: () => this.listFeedbacks(),
        error: () => this.handleError('Error saving feedback'),
      });
    }
  }

  scrollToFeedbacks(): void {
    if (this.feedbackSection) {
      this.listFeedbacks();
      this.feedbackSection.nativeElement.scrollIntoView({ behavior: 'smooth' });
    }
  }

  private listFeedbacks(): void {
    this.isLoadingComments = true;

    this.updatePaginationParams();

    this.feedbackService
      .getFeedbacksByProduct(this.product.id, this.paginationParams)
      .subscribe({
        next: (data) => this.updateFeedbacks(data),
        error: () => {
          this.isLoadingComments = false;
          this.handleError('Error loading feedbacks');
        },
      });
  }

  private updatePaginationParams(): void {
    this.paginationParams.page = this.page.number - 1;
    this.paginationParams.size = this.page.size;
  }

  private updateFeedbacks(data: FeedbackResponse): void {
    this.feedbacks = data.content;
    this.page = {
      number: data.page.number + 1,
      size: data.page.size,
      totalElements: data.page.totalElements,
    };
    this.isLoadingComments = false;
  }

  private handleError(message: string): void {
    this.notificationService.showMessage(message, false);
  }

  private initializeUser(): void {
    this.tokenService.getUserByJwtToken()?.subscribe((user) => {
      this.user = user;
      if (this.user) {
        this.isNotLoggedIn = false;
        this.checkIfPurchased();
      }
    });
  }

  private checkIfPurchased(): void {
    this.userService
      .hasUserPurchasedProduct(this.user.id, this.product.id)
      .subscribe((isPurchased) => {
        this.isPurchasedProduct = isPurchased;
      });
  }

  private isFeedbackValid(): boolean {
    return !!this.feedback.comment?.trim() && !!this.feedback.note;
  }
}
