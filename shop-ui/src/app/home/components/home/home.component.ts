import { PaginationAndSortParams } from '../../../pagination/models/pagination-and-sort-params/pagination-and-sort-params';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Product } from '../../../product/models/product/product';
import { ProductService } from '../../../product/services/product/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  @ViewChild('trendingSection') trendingSection: ElementRef | undefined;

  paginationAndSortParams: PaginationAndSortParams = {
    page: 0,
    size: 5,
    sortBy: 'SALES_DESC',
  };

  isPageLoading: boolean = true;
  isProductsLoading: boolean = true;
  products: Product[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
    this.registerPageLoadEvent();
  }

  scrollToTrending(): void {
    this.trendingSection?.nativeElement.scrollIntoView({ behavior: 'smooth' });
  }

  isProductNew(dateCreated: Date): boolean {
    return this.productService.isProductNew(dateCreated);
  }

  private loadProducts(): void {
    this.isProductsLoading = true;
    this.productService
      .getAllProducts(this.paginationAndSortParams)
      .subscribe((response) => {
        this.products = response.content;
        this.isProductsLoading = false;
      });
  }

  private registerPageLoadEvent(): void {
    if (document.readyState === 'complete') {
      this.updatePageLoadState();
    } else {
      window.addEventListener('load', this.updatePageLoadState.bind(this));
    }
  }

  private updatePageLoadState(): void {
    const container = document.querySelector('.container');
    this.isPageLoading = !container;
  }
}
