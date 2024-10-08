import { TokenService } from './../../services/token/token.service';
import { Component, HostListener, OnInit } from '@angular/core';
import { ProductCategory } from '../../common/product-category/product-category';
import { ProductService } from '../../services/product/product.service';
import { Router } from '@angular/router';
import { NotificationService } from '../../services/notification/notification.service';
import { User } from '../../common/user/user';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrl: './navigation-bar.component.scss',
})
export class NavigationBarComponent implements OnInit {
  productCategories: ProductCategory[] = [];
  isUserLoggedIn: boolean = false;
  isNotificationVisible: boolean = false;
  isMobile: boolean = false;
  isCategoryButtonClicked: boolean = false;
  isDifferentSearchMode: boolean = false;
  isAdmin: boolean = false;

  constructor(
    private tokenService: TokenService,
    private productService: ProductService,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.tokenService.getUserInfo()?.subscribe((userInfo) => {
      this.isAdmin =
        userInfo?.roles.some((role) => role.name === 'ADMIN') || false;
    });
    this.tokenService.isLoggedIn.subscribe((loggedIn) => {
      this.isUserLoggedIn = loggedIn;
    });
    this.notificationService.isVisible$.subscribe((visible) => {
      this.isNotificationVisible = visible;
    });
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
    const differentSearch = document.querySelector('.max-width-search');
    header?.classList.toggle('sticky', window.scrollY > 0);
    if (differentSearch?.classList.contains('sticky') && window.scrollY <= 0) {
      differentSearch?.classList.remove('sticky');
    }
    if (!differentSearch?.classList.contains('sticky') && window.scrollY > 0) {
      differentSearch?.classList.add('sticky');
    }
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    this.checkWindowWidth();
  }

  @HostListener('window:load', [])
  onLoad() {
    const iconContainer = document.querySelector('.nav-icon');
    if (iconContainer) {
      iconContainer.classList.remove('loading');
      iconContainer.classList.add('loaded');
    }

    if (window.innerWidth <= 768) {
      this.isMobile = true;
    }
  }

  checkWindowWidth() {
    if (
      window.innerWidth <= 1000 &&
      document
        .querySelector('.search-bar')
        ?.classList.contains('active-search-bar')
    ) {
      document.querySelector('header')?.classList.add('mobile-search-active');
      this.isMobile = true;
    } else {
      document
        .querySelector('header')
        ?.classList.remove('mobile-search-active');
      this.isMobile = false;

      if (window.innerWidth <= 768) {
        this.isMobile = true;
      } else {
        this.isMobile = false;
        this.isCategoryButtonClicked = false;
      }
    }

    if (window.innerWidth <= 600) {
      this.isDifferentSearchMode = true;
      const differentSearch = document.querySelector('.max-width-search');

      if (
        differentSearch?.classList.contains('sticky') &&
        window.scrollY <= 0
      ) {
        differentSearch?.classList.remove('sticky');
      }
      if (
        !differentSearch?.classList.contains('sticky') &&
        window.scrollY > 0
      ) {
        differentSearch?.classList.add('sticky');
      }
    } else {
      this.isDifferentSearchMode = false;
    }
  }

  toggleMenu() {
    document.querySelector('#menu-icon')?.classList.toggle('bx-x');
    document.querySelector('.navmenu')?.classList.toggle('open');
    if (this.isCategoryButtonClicked) {
      this.isCategoryButtonClicked = false;
    }
  }

  toggleSearch() {
    document
      .querySelector('.search-bar')
      ?.classList.toggle('active-search-bar');
    document
      .querySelector('.search-input')
      ?.classList.toggle('active-search-input');
    document.querySelector('.bx-x')?.classList.toggle('disabled');

    if (
      window.innerWidth <= 1000 &&
      window.getComputedStyle(document.querySelector('.navmenu')!).position ===
        'relative'
    ) {
      document.querySelector('header')?.classList.add('mobile-search-active');
      this.isMobile = true;
    }

    if (
      window.innerWidth <= 768 &&
      window.getComputedStyle(document.querySelector('.navmenu')!).position ===
        'absolute'
    ) {
      document
        .querySelector('header')
        ?.classList.remove('mobile-search-active');
      this.isMobile = false;
    }

    this.checkWindowWidth();
  }

  doSearch(value: string) {
    if (
      document
        .querySelector('.search-bar')
        ?.classList.contains('active-search-bar') ||
      this.isDifferentSearchMode
    ) {
      this.router.navigateByUrl(`/search/${value}`);
    }
    this.toggleSearch();
  }

  handleCategoryButton() {
    if (!this.isCategoryButtonClicked && this.isMobile) {
      this.isCategoryButtonClicked = true;
    }
  }

  logout() {
    this.tokenService.logout();
    this.router.navigate(['']);
  }
}
