import { PaginationAndSortParams } from '../../../pagination/models/pagination-and-sort-params/pagination-and-sort-params';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../../token/services/token/token.service';
import { Product } from '../../../product/models/product/product';
import { User } from '../../../user/models/user/user';
import { PaginatedProductResponse } from '../../../product/models/paginated-product-response/paginated-product-response';
import { NotificationService } from '../../../notification/services/notification/notification.service';
import { FavouriteService } from '../../../favourite/services/favourite/favourite.service';

@Component({
  selector: 'app-favourite-product',
  templateUrl: './favourite-product.component.html',
  styleUrl: './favourite-product.component.scss',
})
export class FavouriteProductComponent implements OnInit {
  @Input() product!: Product;

  paginationAndSortParams: PaginationAndSortParams = { page: 0, size: 100 };
  user: User = new User();
  products: Product[] = [];
  isHovered: boolean = false;

  constructor(
    private favouriteService: FavouriteService,
    private tokenService: TokenService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadUserAndFavourites();
  }

  addFavourite(): void {
    if (this.isUserLoggedIn()) {
      this.favouriteService
        .addFavouriteProduct(this.user.id, this.product.id)
        .subscribe({
          next: () => this.addProductToFavourites(this.product),
          error: () => this.handleError('Error adding product to favourites'),
        });
    } else {
      this.navigateToLogin();
    }
  }

  removeFavourite(): void {
    if (this.isUserLoggedIn()) {
      this.favouriteService
        .removeFavouriteProduct(this.user.id, this.product.id)
        .subscribe({
          next: () => this.removeProductFromFavourites(this.product.id),
          error: () =>
            this.handleError('Error removing product from favourites'),
        });
    } else {
      this.navigateToLogin();
    }
  }

  isFavourited(): boolean {
    return this.products.some((p) => p.id === this.product.id);
  }

  onMouseEnter() {
    this.isHovered = true;
  }

  onMouseLeave() {
    this.isHovered = false;
  }

  private loadUserAndFavourites(): void {
    this.tokenService.getUserByJwtToken()?.subscribe({
      next: (data) => {
        this.user = data;
        this.fetchFavourites();
      },
      error: () => this.navigateToLogin(),
    });
  }

  private fetchFavourites(): void {
    this.favouriteService
      .getFavourites(this.user.id, this.paginationAndSortParams)
      .subscribe({
        next: (data: PaginatedProductResponse) =>
          (this.products = data.content),
        error: () => this.handleError('Error fetching favourites'),
      });
  }

  private handleError(message: string): void {
    this.notificationService.showMessage(message, false);
  }

  private navigateToLogin(): void {
    this.router.navigate(['login']);
  }

  private isUserLoggedIn(): boolean {
    return this.user.id !== 0;
  }

  private addProductToFavourites(product: Product): void {
    this.products.push(product);
  }

  private removeProductFromFavourites(productId: number): void {
    this.products = this.products.filter((p) => p.id !== productId);
  }
}
