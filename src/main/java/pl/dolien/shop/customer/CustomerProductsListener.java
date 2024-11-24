package pl.dolien.shop.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerProductsListener {

    private final CustomerService customerService;

    @KafkaListener(topics = "update-customer-products", groupId = "order-sent-group")
    public void associateProductsWithCustomer(CustomerProductsEvent event) {
        customerService.updatePurchasedProducts(event.getCustomer(), event.getOrderItems());
    }
}

