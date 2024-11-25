import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductCategory } from '../../../product/models/product-category/product-category';
import { TokenService } from '../../../token/services/token/token.service';
import { ProductService } from '../../../product/services/product/product.service';
import { NotificationService } from '../../../notification/services/notification/notification.service';
import { RoleService } from '../../../role/services/role/role.service';

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
    private notificationService: NotificationService,
    private roleService: RoleService
  ) {}

  ngOnInit(): void {
    this.initializeUserState();
    this.initalizeNotificationVisibility();
    this.showLoginOrLogoutMessage();
    this.loadProductCategories();
    this.checkWindowWidth();
  }

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    this.toggleStickyHeader();
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.checkWindowWidth();
  }

  @HostListener('window:load', [])
  onLoad(): void {
    this.handleIconLoad();
    this.checkWindowWidth();
  }

  toggleMenu(): void {
    this.toggleClass('#menu-icon', 'bx-x');
    this.toggleClass('.navmenu', 'open');
    this.isCategoryButtonClicked = false;
  }

  toggleSearch(): void {
    const searchBar = document.querySelector('.search-bar');
    const searchInput = document.querySelector('.search-input');
    const closeIcon = document.querySelector('.bx-x');

    searchBar?.classList.toggle('active-search-bar');
    searchInput?.classList.toggle('active-search-input');
    closeIcon?.classList.toggle('disabled');

    if (window.innerWidth <= 1000) {
      this.toggleMobileSearch();
    }

    this.checkWindowWidth();
  }

  doSearch(value: string): void {
    if (this.isSearchActive()) {
      this.router.navigateByUrl(`/search/${value}`);
    }
    this.toggleSearch();
  }

  handleCategoryButton(): void {
    if (this.isMobile) {
      this.isCategoryButtonClicked = true;
    }
  }

  logout() {
    this.tokenService.logout();
    this.router.navigate(['']);
  }

  private initializeUserState(): void {
    this.tokenService.getUserByJwtToken()?.subscribe((userInfo) => {
      this.isAdmin = this.roleService.hasAdminRole(userInfo);
    });

    this.tokenService.isLoggedIn.subscribe((loggedIn) => {
      this.isUserLoggedIn = loggedIn;
    });
  }

  private initalizeNotificationVisibility(): void {
    this.notificationService.isVisible$.subscribe((visible) => {
      this.isNotificationVisible = visible;
    });
  }

  private showLoginOrLogoutMessage(): void {
    this.showMessageFromLocalStorage('loginMessage');
    this.showMessageFromLocalStorage('logoutMessage');
  }

  private showMessageFromLocalStorage(key: string): void {
    const message = localStorage.getItem(key);
    if (message) {
      this.notificationService.showMessage(message, true);
      localStorage.removeItem(key);
    }
  }

  private loadProductCategories() {
    this.productService.getProductCategories().subscribe((data) => {
      this.productCategories = data;
    });
  }

  private checkWindowWidth(): void {
    const width = window.innerWidth;
    this.isMobile = width <= 768;
    this.isDifferentSearchMode = width <= 600;

    const header = document.querySelector('header');
    const searchBar = document.querySelector('.search-bar');

    if (this.isMobile && searchBar?.classList.contains('active-search-bar')) {
      header?.classList.add('mobile-search-active');
    } else {
      header?.classList.remove('mobile-search-active');
      this.isCategoryButtonClicked = false;
    }
  }

  private toggleStickyHeader(): void {
    const isScrolled = window.scrollY > 0;

    this.toggleClass('header', 'sticky', isScrolled);
    this.toggleClass('.max-width-search', 'sticky', isScrolled);
  }

  private toggleClass(
    element: string | Element,
    className: string,
    condition: boolean = true
  ): void {
    const el =
      typeof element === 'string' ? document.querySelector(element) : element;
    el?.classList.toggle(className, condition);
  }

  private handleIconLoad(): void {
    const iconContainer = document.querySelector('.nav-icon');
    if (iconContainer) {
      iconContainer.classList.remove('loading');
      iconContainer.classList.add('loaded');
    }
  }

  private toggleMobileSearch(): void {
    const header = document.querySelector('header');
    const navMenu = document.querySelector('.navmenu');

    if (navMenu && window.getComputedStyle(navMenu).position === 'relative') {
      header?.classList.add('mobile-search-active');
    } else {
      header?.classList.remove('mobile-search-active');
    }
  }

  private isSearchActive(): boolean {
    return (
      document
        .querySelector('.search-bar')
        ?.classList.contains('active-search-bar') || this.isDifferentSearchMode
    );
  }
}
