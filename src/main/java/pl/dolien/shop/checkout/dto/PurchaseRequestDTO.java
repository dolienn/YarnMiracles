package pl.dolien.shop.checkout.dto;

import lombok.*;
import pl.dolien.shop.address.Address;
import pl.dolien.shop.customer.Customer;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;

import java.util.Set;

@Getter
@Setter
@Builder
public class PurchaseRequestDTO {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
