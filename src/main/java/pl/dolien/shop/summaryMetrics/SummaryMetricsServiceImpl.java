package pl.dolien.shop.summaryMetrics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.exception.SummaryMetricsNotFoundException;
import pl.dolien.shop.order.Order;

@Service
@RequiredArgsConstructor
public class SummaryMetricsServiceImpl implements SummaryMetricsService {

    private final SummaryMetricsRepository repository;

    @Override
    public SummaryMetrics getSummaryMetrics() {
        return repository.findById(1L).orElseThrow(() -> new SummaryMetricsNotFoundException("Summary metrics not found"));
    }

    @Transactional
    @Override
    public void updateOrderMetrics(Order order) {
        SummaryMetrics metrics = getSummaryMetrics();
        metrics.setTotalOrders(metrics.getTotalOrders() + 1);
        metrics.setRevenue(metrics.getRevenue().add(order.getTotalPrice()));
        metrics.setTotalProductsSold(metrics.getTotalProductsSold() + order.getTotalQuantity());
    }

    @Transactional
    @Override
    public void incrementUserCount(Integer quantity) {
        SummaryMetrics metrics = getSummaryMetrics();
        metrics.setTotalUsers(metrics.getTotalUsers() + quantity);
    }

    @Transactional
    @Override
    public void incrementCustomerFeedbackCount(Integer quantity) {
        SummaryMetrics metrics = getSummaryMetrics();
        metrics.setTotalCustomerFeedback(metrics.getTotalCustomerFeedback() + quantity);
    }
}
