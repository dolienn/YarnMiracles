package pl.dolien.shop.address;

import jakarta.persistence.*;
import lombok.*;
import pl.dolien.shop.order.Order;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String city;
    private String country;
    private String zipCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Order order;
}
