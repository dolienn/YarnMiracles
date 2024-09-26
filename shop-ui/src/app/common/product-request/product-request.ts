import { ProductCategory } from '../product-category/product-category';

export class ProductRequest {
  public category: ProductCategory = new ProductCategory(0, '');
  public name: string = '';
  public description: string = '';
  public unitPrice: number = 0;
  public unitsInStock: number = 0;
}
