import { Component, HostListener, OnInit } from '@angular/core';
import { Product } from '../../common/product/product';
import { ProductService } from '../../services/product/product.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.scss',
})
export class ProductDetailsComponent implements OnInit {
  product!: Product;
  value = 1;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.handleProductDetails();
    });
  }

  handleProductDetails() {
    const id: number = +this.route.snapshot.paramMap.get('id')!;

    this.productService.getProduct(id).subscribe((data) => {
      this.product = data;
    });
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    if (
      event.key !== 'ArrowUp' &&
      event.key !== 'ArrowDown' &&
      event.key !== 'Tab' &&
      event.key !== 'Backspace'
    ) {
      event.preventDefault();
    }
  }
}
