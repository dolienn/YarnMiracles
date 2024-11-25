package pl.dolien.shop.summaryMetrics;

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
public class SummaryMetrics {

    @Id
    private Long id;
    private int totalUsers;
    private int totalOrders;
    private int totalCustomerFeedback;
    private int totalProductsSold ;
    private BigDecimal revenue;
}
