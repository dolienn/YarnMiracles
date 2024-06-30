import { Component, Input, OnInit } from '@angular/core';
import { FeedbackService } from '../../services/feedback/feedback.service';
import { Feedback } from '../../common/feedback/feedback';
import { Product } from '../../common/product/product';
import { FeedbackRequest } from '../../common/feedback-request/feedback-request';
import { HoverRatingChangeEvent, RatingChangeEvent } from 'angular-star-rating';
import { TokenService } from '../../services/token.service';
import { UserService } from '../../services/user/user.service';
import { User } from '../../common/user/user';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.scss',
})
export class FeedbackComponent implements OnInit {
  feedbacks: Feedback[] = [];
  pageNumber: number = 1;
  pageSize: number = 6;
  totalElements: number = 0;

  feedback: FeedbackRequest = new FeedbackRequest();
  user: User = new User();

  @Input()
  product!: Product;

  constructor(
    private feedbackService: FeedbackService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.listFeedbacks();
  }

  ratingChange(event: RatingChangeEvent): void {
    this.feedback.note = event.rating;
  }

  listFeedbacks() {
    this.feedbackService
      .getFeedbacksByProduct(
        this.pageNumber - 1,
        this.pageSize,
        this.product.id
      )
      .subscribe((data: any) => {
        this.feedbacks = data.content;
        this.pageNumber = data.number + 1;
        this.pageSize = data.size;
        this.totalElements = data.totalElements;

        this.feedbacks.forEach((feedback) => {
          this.getUser(feedback.createdBy || 0).subscribe((user: User) => {
            feedback.createdByUser = user;
          });
        });
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
}
