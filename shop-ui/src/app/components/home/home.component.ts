import { Component, HostListener, OnInit } from '@angular/core';
import { Product } from '../../common/product/product';
import { ProductService } from '../../services/product/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  isLoading: boolean = true;
  isLoadingProducts: boolean = true;

  products: Product[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.listProducts();
  }

  listProducts() {
    this.isLoadingProducts = true;
    this.productService.getProductList(1).subscribe((data) => {
      this.products = data;
      this.isLoadingProducts = false;
    });
  }

  @HostListener('window:load', [])
  onLoad() {
    const container = document.querySelector('.container');
    if (container) {
      this.isLoading = false;
    }
  }
}
