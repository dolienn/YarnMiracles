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
    this.checkWindowWidth();
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

  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    this.checkWindowWidth();
  }

  checkWindowWidth() {
    if (
      window.innerWidth <= 1000 &&
      document
        .querySelector('.search-bar')
        ?.classList.contains('active-search-bar')
    ) {
      document.querySelector('header')?.classList.add('mobile-search-active');
    } else {
      document
        .querySelector('header')
        ?.classList.remove('mobile-search-active');
    }
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

    if (
      window.innerWidth <= 1000 &&
      window.getComputedStyle(document.querySelector('.navmenu')!).position ===
        'relative'
    ) {
      console.log('xd');
      document.querySelector('header')?.classList.add('mobile-search-active');
    }

    if (
      window.innerWidth <= 750 &&
      window.getComputedStyle(document.querySelector('.navmenu')!).position ===
        'absolute'
    ) {
      document
        .querySelector('header')
        ?.classList.remove('mobile-search-active');
    }
  }
}
