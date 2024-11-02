package pl.dolien.shop.order;

import org.springframework.stereotype.Service;
import pl.dolien.shop.purchase.Purchase;

import java.util.UUID;

@Service
public class OrderSetupService {

    public Order initializeOrder(Purchase purchase) {
        Order order = purchase.getOrder();
        order.setOrderTrackingNumber(generateOrderTrackingNumber());
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());
        order.setStatus("pending");
        return order;
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
