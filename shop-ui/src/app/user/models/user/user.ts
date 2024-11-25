import { Feedback } from '../../../feedback/models/feedback/feedback';
import { Product } from '../../../product/models/product/product';
import { Role } from '../../../role/models/role/role';

export class User {
  constructor(
    public id: number = 0,
    public firstname: string = '',
    public lastname: string = '',
    public email: string = '',
    public dateOfBirth: Date = new Date(),
    public accountLocked: boolean = false,
    public feedbacks: Feedback[] = [],
    public quantityOfPurchasedProducts: number = 0,
    public roles: Role[] = []
  ) {}
}
