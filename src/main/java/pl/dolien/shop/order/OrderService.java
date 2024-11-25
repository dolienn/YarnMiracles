package pl.dolien.shop.order;

import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;

import java.util.Set;

public interface OrderService {

    Order buildOrder(PurchaseRequestDTO purchase);

    void addOrderItems(Order order, Set<OrderItem> orderItems);

    Order saveOrder(Order order);
}

