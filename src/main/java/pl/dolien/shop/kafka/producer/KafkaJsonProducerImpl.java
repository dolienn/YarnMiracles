package pl.dolien.shop.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import pl.dolien.shop.customer.CustomerProductsEvent;
import pl.dolien.shop.email.activationAccount.AccountActivationMessageDTO;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;
import pl.dolien.shop.order.OrderItemsEvent;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class KafkaJsonProducerImpl implements KafkaJsonProducer {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Override
    public void sendMessageToUpdateOrderMetrics(Order order) {
        Message<Order> message = MessageBuilder
                .withPayload(order)
                .setHeader(KafkaHeaders.TOPIC, "update-order-metrics")
                .build();

        kafkaTemplate.send(message);
    }

    @Override
    public void sendMessageToUpdateCustomerProducts(CustomerProductsEvent event) {
        Message<CustomerProductsEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "update-customer-products")
                .build();

        kafkaTemplate.send(message);
    }

    @Override
    public void sendMessageToUpdateInventory(Set<OrderItem> orderItems) {
        OrderItemsEvent event = new OrderItemsEvent(orderItems);
        Message<OrderItemsEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "update-inventory")
                .build();

        kafkaTemplate.send(message);
    }

    @Override
    public void sendMessageToSendActivationEmail(AccountActivationMessageDTO activationMessageDTO) {
        Message<AccountActivationMessageDTO> message = MessageBuilder
                .withPayload(activationMessageDTO)
                .setHeader(KafkaHeaders.TOPIC, "send-activation-email")
                .build();

        kafkaTemplate.send(message);
    }

    @Override
    public void sendMessageToIncrementUserCount(Integer quantity) {
        Message<Integer> message = MessageBuilder
                .withPayload(quantity)
                .setHeader(KafkaHeaders.TOPIC, "increment-user-count")
                .build();

        kafkaTemplate.send(message);
    }

    @Override
    public void sendMessageToIncrementCustomerFeedbackCount(Integer quantity) {
        Message<Integer> message = MessageBuilder
                .withPayload(quantity)
                .setHeader(KafkaHeaders.TOPIC, "increment-customer-feedback-count")
                .build();

        kafkaTemplate.send(message);
    }
}
