import { Address } from '../../../address/models/address/address';
import { Customer } from '../customer/customer';
import { OrderItem } from '../../../order/models/order-item/order-item';
import { Order } from '../../../order/models/order/order';

export class Purchase {
  constructor(
    public customer?: Customer,
    public shippingAddress?: Address,
    public billingAddress?: Address,
    public order?: Order,
    public orderItems?: OrderItem[]
  ) {}
}
