import { Product } from './../../common/product/product';
import { Component, Input, OnInit } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { TokenService } from '../../services/token.service';
import { User } from '../../common/user/user';

@Component({
  selector: 'app-favourite-product',
  templateUrl: './favourite-product.component.html',
  styleUrl: './favourite-product.component.scss',
})
export class FavouriteProductComponent implements OnInit {
  constructor(
    private userService: UserService,
    private tokenService: TokenService
  ) {}

  @Input()
  product!: Product;

  user: User = new User();

  products: Product[] = [];

  isHovered: boolean = false;

  ngOnInit() {
    console.log(this.product);
    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user = data;
      this.getFavourites();
    });
  }

  addFavourite() {
    this.userService
      .addFavouriteProduct(this.user.id, +this.product.id)
      .subscribe({
        next: () => {
          this.products.push(this.product);
        },
        error: (err) =>
          console.error('Error adding product to favourites', err),
      });
  }

  removeFavourite() {
    this.userService
      .removeFavouriteProduct(this.user.id, +this.product.id)
      .subscribe({
        next: () => {
          this.products = this.products.filter((p) => p.id !== this.product.id);
        },
        error: (err) =>
          console.error('Error removed product to favourites', err),
      });
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
