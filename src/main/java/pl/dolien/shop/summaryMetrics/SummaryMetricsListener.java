package pl.dolien.shop.summaryMetrics;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.dolien.shop.order.Order;

@Component
@RequiredArgsConstructor
public class SummaryMetricsListener {

    private final SummaryMetricsService summaryMetricsService;

    @KafkaListener(topics = "update-order-metrics", groupId = "myGroup")
    public void updateOrderMetrics(Order order) {
        summaryMetricsService.updateOrderMetrics(order);
    }

    @KafkaListener(topics = "increment-user-count", groupId = "myGroup")
    public void incrementUserCount(Integer quantity) {
        summaryMetricsService.incrementUserCount(quantity);
    }

    @KafkaListener(topics = "increment-customer-feedback-count", groupId = "myGroup")
    public void incrementCustomerFeedbackCount(Integer quantity) {
        summaryMetricsService.incrementCustomerFeedbackCount(quantity);
    }
}
