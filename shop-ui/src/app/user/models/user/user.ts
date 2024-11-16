import { Feedback } from '../../../feedback/models/feedback/feedback';
import { Product } from '../../../product/models/product/product';
import { Role } from '../role/role';

export class User {
  public id: number = 0;
  public firstname: string = '';
  public lastname: string = '';
  public email: string = '';
  public dateOfBirth: Date = new Date();
  public accountLocked: boolean = false;
  public feedbacks: Feedback[] = [];
  public purchasedProducts: Product[] = [];
  public roles: Role[] = [];
}
