import { Order } from '../order/order';

export class OrderHistory extends Order {
  public id: string = '';
  public orderTrackingNumber: string = '';
  public dateCreated: Date = new Date();
}
