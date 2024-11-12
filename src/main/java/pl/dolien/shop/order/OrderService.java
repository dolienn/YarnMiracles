package pl.dolien.shop.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductInventoryUpdater productInventoryService;

    @Transactional
    public void addOrderItems(Order order, Set<OrderItem> orderItems) {
        orderItems.forEach(order::add);
        orderItems.forEach(productInventoryService::updateProductInventory);
    }
}
