package pl.dolien.shop.order;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final KafkaTemplate<String, OrderItem> kafkaTemplate;

    @Transactional
    public void addOrderItems(Order order, Set<OrderItem> orderItems) {
        orderItems.forEach(order::add);
        orderItems.forEach(item -> kafkaTemplate.send("inventory-update", item.getId().toString(), item));
    }
}
