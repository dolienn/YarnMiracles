import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FeedbackService } from '../../services/feedback/feedback.service';
import { Feedback } from '../../common/feedback/feedback';
import { Product } from '../../common/product/product';
import { FeedbackRequest } from '../../common/feedback-request/feedback-request';
import { RatingChangeEvent } from 'angular-star-rating';
import { TokenService } from '../../services/token/token.service';
import { UserService } from '../../services/user/user.service';
import { User } from '../../common/user/user';
import { Observable, forkJoin, map, switchMap } from 'rxjs';

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

  ratingChange(event: RatingChangeEvent): void {
    this.feedback.note = event.rating;
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
    this.feedback.productId = this.product.id;
    this.feedbackService.saveFeedback(this.feedback).subscribe({
      next: (response) => {
        console.log('Feedback saved with id:', response);
        this.listFeedbacks();
      },
      error: (err) => console.error('Error saving feedback:', err.error),
    });
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
