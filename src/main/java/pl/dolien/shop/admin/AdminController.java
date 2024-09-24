package pl.dolien.shop.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.dashboard.DashboardData;
import pl.dolien.shop.dashboard.DashboardService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final DashboardService dashboardService;

    @GetMapping("/add-admin")
    public ResponseEntity<?> addAdmin(@RequestParam String email) {
        adminService.addAdmin(email);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/get-dashboard-data")
    public ResponseEntity<DashboardData> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
}
