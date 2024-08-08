import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-successful-purchase',
  templateUrl: './successful-purchase.component.html',
  styleUrl: './successful-purchase.component.scss',
})
export class SuccessfulPurchaseComponent implements OnInit {
  orderDetails: any;
  purchase: any;

  shipping: number = 19.99;

  constructor(private router: Router) {}

  ngOnInit(): void {
    const orderDetails = sessionStorage.getItem('orderDetails');
    const purchase = sessionStorage.getItem('purchase');

    if (orderDetails && purchase) {
      this.orderDetails = JSON.parse(orderDetails);
      this.purchase = JSON.parse(purchase);
    } else {
      this.router.navigate(['/']);
    }
  }
}
