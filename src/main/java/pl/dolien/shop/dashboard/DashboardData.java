package pl.dolien.shop.dashboard;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DashboardData {

    @Id
    private Long id;
    private int totalUsers;
    private int totalOrders;
    private int totalCustomerFeedback;
    private int productsSell;
    private BigDecimal revenue;
}
