import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Product } from '../../common/product/product';
import { ProductService } from '../../services/product/product.service';
import { ActivatedRoute } from '@angular/router';
import { CartItem } from '../../common/cart-item/cart-item';
import { CartService } from '../../services/cart/cart.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.scss',
})
export class ProductDetailsComponent implements OnInit {
  isLoading: boolean = true;

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

  addToCart(product: Product) {
    const cartItem = new CartItem(product);

    this.cartService.addToCart(cartItem);
  }
}
