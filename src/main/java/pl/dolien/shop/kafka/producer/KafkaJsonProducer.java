package pl.dolien.shop.kafka.producer;

import pl.dolien.shop.customer.CustomerProductsEvent;
import pl.dolien.shop.email.activationAccount.AccountActivationMessageDTO;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;

import java.util.Set;

public interface KafkaJsonProducer {

    void sendMessageToUpdateOrderMetrics(Order order);

    void sendMessageToUpdateCustomerProducts(CustomerProductsEvent event);

    void sendMessageToUpdateInventory(Set<OrderItem> orderItems);

    void sendMessageToSendActivationEmail(AccountActivationMessageDTO activationMessageDTO);

    void sendMessageToIncrementUserCount(Integer quantity);

    void sendMessageToIncrementCustomerFeedbackCount(Integer quantity);
}

