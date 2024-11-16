import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../user/services/user/user.service';
import { TokenService } from '../../../token/services/token/token.service';
import { Product } from '../../../product/models/product/product';
import { User } from '../../../user/models/user/user';

@Component({
  selector: 'app-favourite-product',
  templateUrl: './favourite-product.component.html',
  styleUrl: './favourite-product.component.scss',
})
export class FavouriteProductComponent implements OnInit {
  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  @Input()
  product!: Product;

  user: User = new User();

  products: Product[] = [];

  isHovered: boolean = false;

  ngOnInit() {
    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user = data;
      this.getFavourites();
    });
  }

  addFavourite() {
    if (this.user.id !== 0) {
      this.userService
        .addFavouriteProduct(this.user.id, +this.product.id)
        .subscribe({
          next: () => {
            this.products.push(this.product);
          },
          error: (err) =>
            console.error('Error adding product to favourites', err),
        });
    } else {
      this.router.navigate(['login']);
    }
  }

  removeFavourite() {
    if (this.user.id !== 0) {
      this.userService
        .removeFavouriteProduct(this.user.id, +this.product.id)
        .subscribe({
          next: () => {
            this.products = this.products.filter(
              (p) => p.id !== this.product.id
            );
          },
          error: (err) =>
            console.error('Error removed product to favourites', err),
        });
    } else {
      this.router.navigate(['login']);
    }
  }

  getFavourites() {
    return this.userService
      .getFavouriteProducts(this.user.id)
      .subscribe((data) => {
        this.products = data;
      });
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
}
