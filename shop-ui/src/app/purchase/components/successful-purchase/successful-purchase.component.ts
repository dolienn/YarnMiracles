import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PurchaseResponseDTO } from '../../models/purchase-reponse-dto/purchase-response-dto';
import { Purchase } from '../../../checkout/models/purchase/purchase';

@Component({
  selector: 'app-successful-purchase',
  templateUrl: './successful-purchase.component.html',
  styleUrl: './successful-purchase.component.scss',
})
export class SuccessfulPurchaseComponent implements OnInit {
  purchase: Purchase = new Purchase();
  purchaseResponse: PurchaseResponseDTO = new PurchaseResponseDTO('');

  shipping: number = 19.99;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.loadPurchaseData();
  }

  private loadPurchaseData(): void {
    const purchaseResponse = sessionStorage.getItem('purchaseResponse');
    const purchase = sessionStorage.getItem('purchase');

    if (purchaseResponse && purchase) {
      this.purchaseResponse = JSON.parse(
        purchaseResponse
      ) as PurchaseResponseDTO;
      this.purchase = JSON.parse(purchase) as Purchase;
    } else {
      this.redirectToHome();
    }
  }

  private redirectToHome(): void {
    this.router.navigate(['']);
  }
}
