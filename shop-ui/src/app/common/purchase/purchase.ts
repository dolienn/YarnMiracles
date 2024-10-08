import { Address } from '../address/address';
import { Customer } from '../customer/customer';
import { OrderItem } from '../order-item/order-item';
import { Order } from '../order/order';

export class Purchase {
  customer?: Customer;
  shippingAddress?: Address;
  billingAddress?: Address;
  order?: Order;
  orderItems?: OrderItem[];
}
