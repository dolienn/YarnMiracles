import { ProductCategory } from '../product-category/product-category';

export class ProductRequest {
  constructor(
    public category: ProductCategory,
    public name: string,
    public description: string,
    public unitPrice: number,
    public unitsInStock: number
  ) {}
}
