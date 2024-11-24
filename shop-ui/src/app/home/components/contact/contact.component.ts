import { Component, OnInit } from '@angular/core';
import { ProductCategory } from '../../../product/models/product-category/product-category';
import { ProductService } from '../../../product/services/product/product.service';
import { NotificationService } from '../../../notification/services/notification/notification.service';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.scss',
})
export class ContactComponent implements OnInit {
  productCategories: ProductCategory[] = [];

  constructor(
    private productService: ProductService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.productService.getProductCategories().subscribe({
      next: (data) => (this.productCategories = data),
      error: () =>
        this.notificationService.showMessage(
          'Error fetching product categories',
          false
        ),
    });
  }
}
