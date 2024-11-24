import { CartItem } from '../../../cart/models/cart-item/cart-item';

export class OrderItem {
  name: string = '';
  imageUrl: string = '';
  quantity: number = 0;
  unitPrice: number = 0;
  productId: number = 0;

  constructor(cartItem: CartItem) {
    this.imageUrl = cartItem.imageUrl;
    this.name = cartItem.name;
    this.quantity = cartItem.quantity;
    this.unitPrice = cartItem.unitPrice;
    this.productId = cartItem.id;
  }
}
