package pl.dolien.shop.order;

import org.springframework.stereotype.Service;
import pl.dolien.shop.checkout.PurchaseRequestDTO;

import java.util.UUID;

import static pl.dolien.shop.order.OrderStatus.PENDING;

@Service
public class OrderSetupService {

    public Order initializeOrder(PurchaseRequestDTO purchase) {
        Order order = purchase.getOrder();
        order.setOrderTrackingNumber(generateOrderTrackingNumber());
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());
        order.setStatus(PENDING);
        return order;
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
