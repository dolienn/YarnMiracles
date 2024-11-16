package pl.dolien.shop.summaryMetrics;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.exception.SummaryMetricsNotFoundException;
import pl.dolien.shop.order.Order;

@Service
@RequiredArgsConstructor
public class SummaryMetricsService {

    private final SummaryMetricsRepository repository;

    @Cacheable(cacheNames = "summaryMetrics", key = "summaryMetrics")
    public SummaryMetrics getSummaryMetrics() {
        return repository.findById(1L).orElseThrow(() -> new SummaryMetricsNotFoundException("Summary metrics not found"));
    }

    @Transactional
    @CacheEvict(cacheNames = "summaryMetrics", key = "summaryMetrics")
    public void updateOrderMetrics(Order order) {
        SummaryMetrics metrics = getSummaryMetrics();
        metrics.setTotalOrders(metrics.getTotalOrders() + 1);
        metrics.setRevenue(metrics.getRevenue().add(order.getTotalPrice()));
        metrics.setProductsSell(metrics.getProductsSell() + order.getTotalQuantity());
    }

    @Transactional
    @CacheEvict(cacheNames = "summaryMetrics", key = "summaryMetrics")
    public void incrementUserCount() {
        SummaryMetrics metrics = getSummaryMetrics();
        metrics.setTotalUsers(metrics.getTotalUsers() + 1);
    }

    @Transactional
    @CacheEvict(cacheNames = "summaryMetrics", key = "summaryMetrics")
    public void incrementCustomerFeedbackCount() {
        SummaryMetrics metrics = getSummaryMetrics();
        metrics.setTotalCustomerFeedback(metrics.getTotalCustomerFeedback() + 1);
    }
}
