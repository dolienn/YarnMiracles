package pl.dolien.shop.summaryMetrics;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SummaryMetricsInitService {

    private final SummaryMetricsRepository repository;

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            SummaryMetrics summaryMetrics = SummaryMetrics.builder()
                    .id(1L)
                    .totalUsers(0)
                    .totalOrders(0)
                    .totalCustomerFeedback(0)
                    .totalProductsSold(0)
                    .revenue(BigDecimal.ZERO)
                    .build();
            repository.save(summaryMetrics);
        }
    }
}
