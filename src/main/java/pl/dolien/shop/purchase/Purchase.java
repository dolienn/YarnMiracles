package pl.dolien.shop.purchase;

import lombok.*;
import pl.dolien.shop.order.Address;
import pl.dolien.shop.order.Customer;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;

import java.util.Set;

@Getter
@Setter
@Builder
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

}
