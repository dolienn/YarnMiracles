import { Feedback } from '../feedback/feedback';
import { Product } from '../product/product';

export class User {
  public id: number = 0;
  public firstname: string = '';
  public lastname: string = '';
  public email: string = '';
  public dateOfBirth: Date = new Date();
  public feedbacks: Feedback[] = [];
  public purchasedProducts: Product[] = [];
}
