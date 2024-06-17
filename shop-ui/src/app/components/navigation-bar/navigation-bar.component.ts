import { Component, HostListener, OnInit } from '@angular/core';
import { ProductCategory } from '../../common/product-category/product-category';
import { ProductService } from '../../services/product/product.service';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrl: './navigation-bar.component.scss',
})
export class NavigationBarComponent implements OnInit {
  productCategories: ProductCategory[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.listProductCategories();
  }

  listProductCategories() {
    this.productService.getProductCategories().subscribe((data) => {
      this.productCategories = data;
    });
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const header = document.querySelector('header');
    header?.classList.toggle('sticky', window.scrollY > 0);
  }

  toggleMenu() {
    document.querySelector('#menu-icon')?.classList.toggle('bx-x');
    document.querySelector('.navmenu')?.classList.toggle('open');
  }

  toggleSearch() {
    document
      .querySelector('.search-bar')
      ?.classList.toggle('active-search-bar');
    document
      .querySelector('.search-input')
      ?.classList.toggle('active-search-input');
  }
}
