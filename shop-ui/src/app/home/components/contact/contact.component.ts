import { Component, OnInit } from '@angular/core';
import { ProductCategory } from '../../../product/models/product-category/product-category';
import { ProductService } from '../../../product/services/product/product.service';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.scss',
})
export class ContactComponent implements OnInit {
  productCategories: ProductCategory[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getProductCategories().subscribe((data) => {
      this.productCategories = data;
    });
  }
}
