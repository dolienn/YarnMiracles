import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../../user/models/user/user';
import { OrderHistory } from '../../models/order-history/order-history';
import { TokenService } from '../../../token/services/token/token.service';
import { OrderHistoryService } from '../../services/order-history/order-history.service';
import { Page } from '../../../shared/models/page/page';
import { PaginationParams } from '../../../pagination/models/pagination-params/pagination-params';
import { OrderHistoryResponse } from '../../models/order-history-response/order-history-response';
import { NotificationService } from '../../../notification/services/notification/notification.service';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrl: './order-history.component.scss',
})
export class OrderHistoryComponent implements OnInit {
  @ViewChild('ordersSection') ordersSection!: ElementRef;

  isLoading: boolean = true;
  user: User = new User();
  orderHistoryList: OrderHistory[] = [];
  page: Page = new Page();
  paginationParams: PaginationParams = new PaginationParams();

  constructor(
    private tokenService: TokenService,
    private orderHistoryService: OrderHistoryService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUserAndOrderHistory();
  }

  scrollToOrders() {
    if (this.ordersSection) {
      this.fetchOrderHistory();
      this.ordersSection.nativeElement.scrollIntoView({
        behavior: 'smooth',
      });
    }
  }

  private loadUserAndOrderHistory(): void {
    this.tokenService.getUserByJwtToken()?.subscribe((data) => {
      this.user = data;
      this.isLoading = false;

      if (this.isUserLoggedIn()) {
        this.fetchOrderHistory();
      } else {
        this.router.navigate(['login']);
      }
    });
  }

  private isUserLoggedIn(): boolean {
    return this.user.id !== 0;
  }

  private fetchOrderHistory(): void {
    const email = this.user.email;

    this.updatePaginationParams();

    this.orderHistoryService
      .getOrderHistory(email, this.paginationParams)
      .subscribe({
        next: (data) => this.updateOrderHistory(data),
        error: () => this.handleError(),
      });
  }

  private updatePaginationParams(): void {
    this.paginationParams.page = this.page.number - 1;
    this.paginationParams.size = this.page.size;
  }

  private updateOrderHistory(data: OrderHistoryResponse): void {
    this.orderHistoryList = data._embedded.orders;
    this.page.number = data.page.number + 1;
    this.page.size = data.page.size;
    this.page.totalElements = data.page.totalElements;
    this.isLoading = false;
  }

  private handleError(): void {
    this.isLoading = false;
    this.notificationService.showMessage(
      'Something went wrong while fetching order history',
      false
    );
  }
}
