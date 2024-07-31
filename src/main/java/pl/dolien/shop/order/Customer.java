package pl.dolien.shop.order;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    public void add(Order order) {
        if(order != null) {
            if(orders == null) {
                orders = new HashSet<>();
            }

            orders.add(order);
            order.setCustomer(this);
        }
    }
}
