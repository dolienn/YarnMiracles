import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { CartItem } from '../../models/cart-item/cart-item';
import { CartStorageService } from '../cart-storage/cart-storage.service';
import { CartUtilsService } from '../cart-utils/cart-utils.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  cartItems: CartItem[] = [];
  totalPrice: Subject<number> = new BehaviorSubject<number>(0);
  totalQuantity: Subject<number> = new BehaviorSubject<number>(0);
  storage: Storage = localStorage;

  constructor(
    private cartStorageService: CartStorageService,
    private cartUtilsService: CartUtilsService
  ) {
    this.cartItems = this.cartStorageService.retrieveCartItems();
    this.computeCartTotals();
  }

  addToCart(cartItem: CartItem) {
    if (this.checkIfItemExistsInCart(cartItem)) {
      this.incrementQuantity(cartItem);
    } else {
      this.cartItems.push(cartItem);
    }

    this.computeCartTotals();
  }

  computeCartTotals() {
    const { totalPrice, totalQuantity } = this.cartUtilsService.computeTotals(
      this.cartItems
    );

    this.totalPrice.next(totalPrice);
    this.totalQuantity.next(totalQuantity);

    this.cartStorageService.saveCartItems(this.cartItems);
  }

  decrementQuantity(cartItem: CartItem) {
    cartItem.quantity--;

    if (cartItem.quantity === 0) {
      this.removeItem(cartItem);
    } else {
      this.computeCartTotals();
    }
  }

  removeItem(cartItem: CartItem) {
    const itemIndex = this.cartItems.findIndex(
      (tempCartItem) => tempCartItem.id === cartItem.id
    );

    if (itemIndex > -1) {
      this.cartItems.splice(itemIndex, 1);

      this.computeCartTotals();
    }
  }

  resetCart() {
    this.cartItems = [];
    this.updateTotals(0, 0);
  }

  private checkIfItemExistsInCart(cartItem: CartItem): boolean {
    if (this.cartItems.length === 0) return false;
    return this.cartItems.find((item) => item.id === cartItem.id) != undefined;
  }

  private incrementQuantity(cartItem: CartItem): void {
    if (cartItem.quantity < this.cartUtilsService.getMaxQuantity(cartItem)) {
      cartItem.quantity++;
    }
  }

  private updateTotals(price: number, quantity: number): void {
    this.totalPrice.next(price);
    this.totalQuantity.next(quantity);
  }
}
