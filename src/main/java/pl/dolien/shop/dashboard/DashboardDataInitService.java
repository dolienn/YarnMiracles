package pl.dolien.shop.dashboard;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardDataInitService {

    private final DashboardDataRepository repository;

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            DashboardData dashboardData = DashboardData.builder()
                    .id(1L)
                    .totalUsers(0)
                    .totalOrders(0)
                    .totalCustomerFeedback(0)
                    .productsSell(0)
                    .revenue(BigDecimal.ZERO)
                    .build();
            repository.save(dashboardData);
        }
    }
}
