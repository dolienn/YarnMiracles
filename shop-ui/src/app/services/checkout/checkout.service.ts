import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Purchase } from '../../common/purchase/purchase';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { PaymentInfo } from '../../common/payment-info/payment-info';

@Injectable({
  providedIn: 'root',
})
export class CheckoutService {
  private readonly purchaseUrl = `${environment.url}/checkout/purchase`;

  private readonly paymentIntentUrl = `${environment.url}/checkout/payment-intent`;

  constructor(private httpClient: HttpClient) {}

  placeOrder(purchase: Purchase): Observable<any> {
    return this.httpClient.post<Purchase>(this.purchaseUrl, purchase);
  }

  createPaymentIntent(paymentInfo: PaymentInfo): Observable<any> {
    return this.httpClient.post<PaymentInfo>(
      this.paymentIntentUrl,
      paymentInfo
    );
  }
}
