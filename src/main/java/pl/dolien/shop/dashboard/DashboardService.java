package pl.dolien.shop.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.exception.DashboardDataNotFoundException;
import pl.dolien.shop.order.Order;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardDataRepository dashboardDataRepository;

    public DashboardData getDashboardData() {
        return dashboardDataRepository.findById(1L).orElseThrow(() -> new DashboardDataNotFoundException("Dashboard data not found"));
    }

    @Transactional
    public void updateOrderMetrics(Order order) {
        DashboardData data = getDashboardData();
        data.setTotalOrders(data.getTotalOrders() + 1);
        data.setThisMonthRevenue(data.getThisMonthRevenue().add(order.getTotalPrice()));
        saveDashboardData(data);
    }

    public void updateProductSales(int quantitySold) {
        DashboardData data = getDashboardData();
        data.setProductsSell(data.getProductsSell() + quantitySold);
        saveDashboardData(data);
    }

    private void saveDashboardData(DashboardData data) {
        dashboardDataRepository.save(data);
    }
}
