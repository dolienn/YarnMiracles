package pl.dolien.shop.dashboard;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/get-dashboard-data")
    public DashboardData getDashboardData() {
        return dashboardService.getDashboardData();
    }
}
