import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Purchase } from '../../models/purchase/purchase';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { PaymentInfo } from '../../../payment/models/payment-info/payment-info';

@Injectable({
  providedIn: 'root',
})
export class CheckoutService {
  private readonly purchaseUrl = `${environment.url}/checkout/purchase`;

  constructor(private httpClient: HttpClient) {}

  placeOrder(purchase: Purchase): Observable<any> {
    return this.httpClient.post<Purchase>(this.purchaseUrl, purchase);
  }
}
