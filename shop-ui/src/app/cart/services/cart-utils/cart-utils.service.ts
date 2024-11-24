import { Injectable } from '@angular/core';
import { CartItem } from '../../models/cart-item/cart-item';

@Injectable({
  providedIn: 'root',
})
export class CartUtilsService {
  computeTotals(cartItems: CartItem[]): {
    totalPrice: number;
    totalQuantity: number;
  } {
    const totalPrice = cartItems.reduce(
      (sum, item) => sum + item.quantity * item.unitPrice,
      0
    );
    const totalQuantity = cartItems.reduce(
      (sum, item) => sum + item.quantity,
      0
    );

    return { totalPrice, totalQuantity };
  }

  getMaxQuantity(cartItem: CartItem): number {
    return Math.min(cartItem.unitsInStock, 9);
  }
}
