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

    private void saveDashboardData(DashboardData data) {
        dashboardDataRepository.save(data);
    }

    @Transactional
    public void updateOrderMetrics(Order order) {
        DashboardData data = getDashboardData();
        data.setTotalOrders(data.getTotalOrders() + 1);
        data.setRevenue(data.getRevenue().add(order.getTotalPrice()));
        saveDashboardData(data);
    }

    public void updateProductSales(int quantitySold) {
        DashboardData data = getDashboardData();
        data.setProductsSell(data.getProductsSell() + quantitySold);
        saveDashboardData(data);
    }

    public void incrementUserCount() {
        DashboardData dashboardData = getDashboardData();
        dashboardData.setTotalUsers(dashboardData.getTotalUsers() + 1);
        saveDashboardData(dashboardData);
    }

    public void incrementCustomerFeedbackCount() {
        DashboardData dashboardData = getDashboardData();
        dashboardData.setTotalCustomerFeedback(dashboardData.getTotalCustomerFeedback() + 1);
        saveDashboardData(dashboardData);
    }
}
