import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../../user/models/user/user';
import { OrderHistory } from '../../models/order-history/order-history';
import { TokenService } from '../../../token/services/token/token.service';
import { OrderHistoryService } from '../../services/order-history/order-history.service';

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

  pageNumber: number = 1;
  pageSize: number = 10;
  totalElements: number = 0;

  constructor(
    private tokenService: TokenService,
    private orderHistoryService: OrderHistoryService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user = data;
      this.isLoading = false;

      if (this.user.id == 0) {
        this.router.navigate(['login']);
      }

      this.handleOrderHistory();
    });
  }

  handleOrderHistory() {
    const email = this.user.email;

    this.orderHistoryService
      .getOrderHistory(this.pageNumber - 1, this.pageSize, email)
      .subscribe((data) => {
        this.orderHistoryList = data._embedded.orders;
        this.pageNumber = data.page.number + 1;
        this.pageSize = data.page.size;
        this.totalElements = data.page.totalElements;
        this.isLoading = false;
      });
  }

  scrollToOrders() {
    if (this.ordersSection) {
      this.handleOrderHistory();
      this.ordersSection.nativeElement.scrollIntoView({
        behavior: 'smooth',
      });
    }
  }
}
