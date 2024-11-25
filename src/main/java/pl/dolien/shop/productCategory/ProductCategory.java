package pl.dolien.shop.productCategory;

import jakarta.persistence.*;
import lombok.*;
import pl.dolien.shop.product.Product;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String categoryName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId")
    private List<Product> products;
}
