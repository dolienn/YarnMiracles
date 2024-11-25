package pl.dolien.shop.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;
import pl.dolien.shop.kafka.producer.KafkaJsonProducer;

import java.util.Set;
import java.util.UUID;

import static pl.dolien.shop.order.OrderStatus.PENDING;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaJsonProducer kafkaJsonProducer;

    @Override
    public Order buildOrder(PurchaseRequestDTO purchase) {
        Order order = purchase.getOrder();
        order.setOrderTrackingNumber(generateOrderTrackingNumber());
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());
        order.setStatus(PENDING);
        return saveOrder(order);
    }

    @Transactional
    @Override
    public void addOrderItems(Order order, Set<OrderItem> orderItems) {
        orderItems.forEach(order::add);
        kafkaJsonProducer.sendMessageToUpdateInventory(orderItems);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
