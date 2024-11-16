import { CartItem } from '../../../cart/models/cart-item/cart-item';

export class OrderItem {
  imageUrl?: string;
  name?: string;
  unitPrice?: number;
  quantity?: number;
  productId?: number;

  constructor(cartItem: CartItem) {
    this.imageUrl = cartItem.imageUrl;
    this.name = cartItem.name;
    this.quantity = cartItem.quantity;
    this.unitPrice = cartItem.unitPrice;
    this.productId = cartItem.id;
  }
}
