import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product/product.service';
import { ProductCategory } from '../../common/product-category/product-category';

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
