package pl.dolien.shop.checkout;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;
import pl.dolien.shop.checkout.dto.PurchaseResponseDTO;
import pl.dolien.shop.customer.CustomerProductsEvent;
import pl.dolien.shop.customer.CustomerService;
import pl.dolien.shop.customer.Customer;
import pl.dolien.shop.kafka.producer.KafkaJsonProducer;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderService;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final KafkaJsonProducer kafkaJsonProducer;

    @Transactional
    @Override
    public PurchaseResponseDTO placeOrder(PurchaseRequestDTO purchase, Authentication connectedUser) {
        Order order = orderService.buildOrder(purchase);
        Customer customer = customerService.processCustomer(order, purchase.getCustomer(), connectedUser);
        orderService.addOrderItems(order, purchase.getOrderItems());

        kafkaJsonProducer.sendMessageToUpdateOrderMetrics(order);
        kafkaJsonProducer.sendMessageToUpdateCustomerProducts(new CustomerProductsEvent(customer, purchase.getOrderItems()));

        return new PurchaseResponseDTO(order.getOrderTrackingNumber());
    }
}
