package pl.dolien.shop.customer;

import org.springframework.security.core.Authentication;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;

import java.util.Set;

public interface CustomerService {

    Customer processCustomer(Order order, Customer customer, Authentication connectedUser);

    void updatePurchasedProducts(Customer customer, Set<OrderItem> orderItems);
}

