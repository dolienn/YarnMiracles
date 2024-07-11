import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Product } from '../../common/product/product';
import { ProductService } from '../../services/product/product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  @ViewChild('trendingSection') trendingSection: ElementRef | undefined;

  isLoading: boolean = true;
  isLoadingProducts: boolean = true;

  products: Product[] = [];

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit(): void {
    this.listProducts();
    if (document.readyState === 'complete') {
      this.onLoad();
    } else {
      window.addEventListener('load', this.onLoad.bind(this));
    }
  }

  listProducts() {
    this.isLoadingProducts = true;
    this.productService.getProductList(1).subscribe((data) => {
      this.products = data;
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
}
