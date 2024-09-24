package pl.dolien.shop.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardDataRepository dashboardDataRepository;

    public DashboardData getDashboardData() {
        return dashboardDataRepository.findById(1L).orElseThrow(() -> new RuntimeException("Dashboard data not found"));
    }
}
