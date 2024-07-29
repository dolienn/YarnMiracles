package pl.dolien.shop.order;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String zipCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Order order;

}
