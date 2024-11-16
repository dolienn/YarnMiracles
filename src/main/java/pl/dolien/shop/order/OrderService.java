package pl.dolien.shop.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;
import pl.dolien.shop.product.ProductInventoryUpdater;

import java.util.Set;
import java.util.UUID;

import static pl.dolien.shop.order.OrderStatus.PENDING;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductInventoryUpdater productInventoryService;

    public Order buildOrder(PurchaseRequestDTO purchase) {
        Order order = purchase.getOrder();
        order.setOrderTrackingNumber(generateOrderTrackingNumber());
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());
        order.setStatus(PENDING);
        return order;
    }

    @Transactional
    public void addOrderItems(Order order, Set<OrderItem> orderItems) {
        orderItems.forEach(order::add);
        orderItems.forEach(productInventoryService::updateProductInventory);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
