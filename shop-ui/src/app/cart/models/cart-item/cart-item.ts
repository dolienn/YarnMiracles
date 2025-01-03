import { Product } from '../../../product/models/product/product';

export class CartItem {
  id: number = 0;
  name: string = '';
  imageUrl: string = '';
  unitPrice: number = 0;
  quantity: number = 1;
  unitsInStock: number = 0;

  constructor(product: Product) {
    this.id = product.id;
    this.name = product.name;
    this.imageUrl = product.imageUrl;
    this.unitPrice = product.unitPrice;
    this.quantity = 1;
    this.unitsInStock = product.unitsInStock;
  }
}
