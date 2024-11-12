package pl.dolien.shop.checkout;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.customer.CustomerService;
import pl.dolien.shop.customer.Customer;
import pl.dolien.shop.dashboard.DashboardService;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderService;
import pl.dolien.shop.order.OrderSetupService;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderSetupService orderSetupService;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Transactional
    public PurchaseResponseDTO placeOrder(PurchaseRequestDTO purchase, Authentication connectedUser) {
        Order order = orderSetupService.initializeOrder(purchase);
        Customer customer = customerService.processCustomer(order, purchase.getCustomer(), connectedUser);
        orderService.addOrderItems(order, purchase.getOrderItems());
        customerService.updatePurchasedProducts(customer, purchase.getOrderItems());

        kafkaTemplate.send("order-created", order.getOrderTrackingNumber(), order);

        return new PurchaseResponseDTO(order.getOrderTrackingNumber());
    }
}
