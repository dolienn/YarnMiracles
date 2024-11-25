import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Product } from '../../models/product/product';
import { ActivatedRoute } from '@angular/router';
import { CartService } from '../../../cart/services/cart/cart.service';
import { ProductService } from '../../services/product/product.service';
import { CartItem } from '../../../cart/models/cart-item/cart-item';
import { NotificationService } from '../../../notification/services/notification/notification.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.scss',
})
export class ProductDetailsComponent implements OnInit {
  @ViewChild('quantityInput') quantityInput!: ElementRef;

  isLoading: boolean = true;
  isPopup: boolean = false;

  product: Product = new Product();
  quantity = 1;

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private notificationService: NotificationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => this.loadProductDetails());
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    if (this.isQuantityInputFocused(event)) {
      this.preventInvalidKeyPress(event);
    }
  }

  maxQuantityInput(): number {
    return Math.min(this.product.unitsInStock, 9);
  }

  addToCart(product: Product): void {
    this.togglePopup();
    this.addProductToCart(product);
  }

  togglePopup(): void {
    this.isPopup = !this.isPopup;
  }

  private loadProductDetails(): void {
    this.isLoading = true;
    const productId = this.getProductIdFromRoute();
    this.fetchProductById(productId);
  }

  private getProductIdFromRoute(): number {
    return +this.route.snapshot.paramMap.get('id')!;
  }

  private fetchProductById(productId: number): void {
    this.productService.getProductById(productId).subscribe({
      next: (data) => {
        this.product = data;
        this.isLoading = false;
      },
      error: () => this.handleError(),
    });
  }

  private handleError(): void {
    this.isLoading = false;
    this.notificationService.showMessage(
      'Something went wrong while fetching product',
      false
    );
  }

  private isQuantityInputFocused(event: KeyboardEvent): boolean {
    return this.quantityInput.nativeElement.contains(event.target);
  }

  private preventInvalidKeyPress(event: KeyboardEvent): void {
    const invalidKeys = ['ArrowUp', 'ArrowDown', 'Tab', 'Backspace'];
    if (!invalidKeys.includes(event.key)) {
      event.preventDefault();
    }
  }

  private addProductToCart(product: Product): void {
    const cartItem = new CartItem(product);
    for (let i = 1; i <= this.quantity; i++) {
      this.cartService.addToCart(cartItem);
    }
  }
}
