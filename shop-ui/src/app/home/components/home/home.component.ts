import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../../../product/models/product/product';
import { ProductService } from '../../../product/services/product/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  @ViewChild('trendingSection') trendingSection: ElementRef | undefined;

  page: number = 0;
  pageSize: number = 5;

  isLoading: boolean = true;
  isLoadingProducts: boolean = true;

  products: Product[] = [];

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit(): void {
    this.listProductsOrderBySales();
    if (document.readyState === 'complete') {
      this.onLoad();
    } else {
      window.addEventListener('load', this.onLoad.bind(this));
    }
  }

  listProductsOrderBySales() {
    this.isLoadingProducts = true;
    this.productService
      .getAllProductsOrderBySales(this.page, this.pageSize)
      .subscribe((data) => {
        this.products = data._embedded.products;
        this.isLoadingProducts = false;
      });
  }

  onLoad() {
    this.isLoading = true;
    const container = document.querySelector('.container');
    if (container) {
      this.isLoading = false;
    }
  }

  scrollToTrending() {
    if (this.trendingSection) {
      this.trendingSection.nativeElement.scrollIntoView({ behavior: 'smooth' });
    }
  }

  isNewProduct(dateCreated: any): boolean {
    return this.productService.isNewProduct(dateCreated);
  }
}
