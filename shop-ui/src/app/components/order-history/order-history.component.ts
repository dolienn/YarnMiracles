import { Component, OnInit } from '@angular/core';
import { OrderHistory } from '../../common/order-history/order-history';
import { User } from '../../common/user/user';
import { TokenService } from '../../services/token/token.service';
import { Router } from '@angular/router';
import { OrderHistoryService } from '../../services/order-history/order-history.service';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrl: './order-history.component.scss',
})
export class OrderHistoryComponent implements OnInit {
  isLoading: boolean = true;

  user: User = new User();

  orderHistoryList: OrderHistory[] = [];

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

    this.orderHistoryService.getOrderHistory(email).subscribe((data) => {
      this.orderHistoryList = data._embedded.orders;
    });
  }
}
