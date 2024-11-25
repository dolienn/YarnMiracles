import { Injectable } from '@angular/core';
import { CartItem } from '../../models/cart-item/cart-item';

@Injectable({
  providedIn: 'root',
})
export class CartStorageService {
  private readonly CART_STORAGE_KEY = 'cartItems';

  retrieveCartItems(): CartItem[] {
    const data = localStorage.getItem(this.CART_STORAGE_KEY);
    return data ? JSON.parse(data) : [];
  }

  saveCartItems(cartItems: CartItem[]): void {
    localStorage.setItem(this.CART_STORAGE_KEY, JSON.stringify(cartItems));
  }
}
