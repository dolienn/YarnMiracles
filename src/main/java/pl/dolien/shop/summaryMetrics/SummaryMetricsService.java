package pl.dolien.shop.summaryMetrics;

import pl.dolien.shop.order.Order;

public interface SummaryMetricsService {

    SummaryMetrics getSummaryMetrics();

    void updateOrderMetrics(Order order);

    void incrementUserCount(Integer quantity);

    void incrementCustomerFeedbackCount(Integer quantity);
}

