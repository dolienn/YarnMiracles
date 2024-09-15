import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FeedbackService } from '../../services/feedback/feedback.service';
import { Feedback } from '../../common/feedback/feedback';
import { Product } from '../../common/product/product';
import { FeedbackRequest } from '../../common/feedback-request/feedback-request';
import { RatingChangeEvent } from 'angular-star-rating';
import { TokenService } from '../../services/token/token.service';
import { UserService } from '../../services/user/user.service';
import { User } from '../../common/user/user';
import { Observable, forkJoin, map, of, switchMap } from 'rxjs';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.scss',
})
export class FeedbackComponent implements OnInit {
  @ViewChild('feedbackSection') feedbackSection!: ElementRef;

  isLoadingComments: boolean = true;

  feedbacks: Feedback[] = [];
  pageNumber: number = 1;
  pageSize: number = 10;
  totalElements: number = 0;
  isNotLoggedIn: boolean = true;

  feedback: FeedbackRequest = new FeedbackRequest();
  user: User = new User();

  @Input()
  product!: Product;

  constructor(
    private feedbackService: FeedbackService,
    private userService: UserService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.listFeedbacks();
    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user = data;
      if (this.user !== null) {
        this.notLoggedIn();
      }
    });
  }

  hasAlreadySentFeedback(): boolean {
    if (!this.user) {
      return false;
    }

    return this.feedbacks.some(
      (feedback) => feedback.createdBy == this.user.id
    );
  }

  ratingChange(event: RatingChangeEvent): void {
    this.feedback.note = event.rating;
  }

  hasPurchasedProduct(): boolean {
    if (!this.product) {
      return false;
    }

    return this.user.purchasedProducts.some(
      (purchasedProduct) => purchasedProduct.id == this.product.id
    );
  }

  listFeedbacks() {
    this.isLoadingComments = true;

    this.feedbackService
      .getFeedbacksByProduct(
        this.pageNumber - 1,
        this.pageSize,
        this.product.id
      )
      .pipe(
        switchMap((data: any) => {
          this.feedbacks = data.content;
          this.pageNumber = data.number + 1;
          this.pageSize = data.size;
          this.totalElements = data.totalElements;

          if (this.feedbacks.length === 0) {
            this.isLoadingComments = false;
            return of([]);
          }

          const userObservables = this.feedbacks.map((feedback) =>
            this.getUser(feedback.createdBy || 0).pipe(
              map((user) => {
                feedback.createdByUser = user;
                return feedback;
              })
            )
          );

          return forkJoin(userObservables);
        })
      )
      .subscribe({
        next: (feedbacksWithUsers) => {
          this.feedbacks = feedbacksWithUsers;
          this.isLoadingComments = false;
        },
        error: (err) => {
          console.error('Error loading feedbacks:', err);
          this.isLoadingComments = false;
        },
      });
  }

  getUser(id: number): Observable<User> {
    return this.userService.getById(id);
  }

  saveFeedback() {
    if (
      this.feedback.comment?.trim() !== null &&
      this.feedback.note !== 0 &&
      this.feedback.note !== null &&
      this.feedback.note !== undefined
    ) {
      this.feedback.productId = this.product.id;
      this.feedbackService.saveFeedback(this.feedback).subscribe({
        next: (response) => {
          this.listFeedbacks();
        },
        error: (err) => console.error('Error saving feedback:', err.error),
      });
    }
  }

  notLoggedIn() {
    this.isNotLoggedIn = false;
  }

  scrollToFeedbacks() {
    if (this.feedbackSection) {
      this.listFeedbacks();
      this.feedbackSection.nativeElement.scrollIntoView({
        behavior: 'smooth',
      });
    }
  }
}
