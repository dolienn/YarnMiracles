package pl.dolien.shop.order;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String imageUrl;

    @Column
    private BigDecimal unitPrice;

    @Column
    private int quantity;

    @Column
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
