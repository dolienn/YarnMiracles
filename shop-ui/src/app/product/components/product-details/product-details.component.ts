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

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.scss',
})
export class ProductDetailsComponent implements OnInit {
  isLoading: boolean = true;
  isPopup: boolean = false;

  product!: Product;
  value = 1;

  @ViewChild('quantityInput') quantityInput!: ElementRef;

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.handleProductDetails();
    });
  }

  handleProductDetails() {
    this.isLoading = true;

    const id: number = +this.route.snapshot.paramMap.get('id')!;

    this.productService.getProduct(id).subscribe((data) => {
      this.product = data;
      this.isLoading = false;
    });
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    const isQuantityInput = this.quantityInput.nativeElement.contains(
      event.target
    );
    if (
      isQuantityInput &&
      event.key !== 'ArrowUp' &&
      event.key !== 'ArrowDown' &&
      event.key !== 'Tab' &&
      event.key !== 'Backspace'
    ) {
      event.preventDefault();
    }
  }

  maxQuantityInput(): number {
    if (this.product.unitsInStock < 9) {
      return this.product.unitsInStock;
    } else {
      return 9;
    }
  }

  addToCart(product: Product) {
    this.togglePopup();

    const cartItem = new CartItem(product);

    for (let i = 1; i <= this.value; i++) {
      this.cartService.addToCart(cartItem);
    }
  }

  togglePopup() {
    this.isPopup = !this.isPopup;
  }
}
