package pl.dolien.shop.product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.user.User;

import java.math.BigDecimal;
import java.util.*;

import static pl.dolien.shop.product.RatingCalculator.calculateRating;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;

    @Column(unique = true)
    private String name;

    private String description;
    private BigDecimal unitPrice;
    private String imageUrl;
    private boolean active;
    private int unitsInStock;
    private Double rate;
    private Long sales = 0L;

    @Column(nullable = false, updatable = false)
    private Integer categoryId;

    @ManyToMany(mappedBy = "favourites")
    private Set<User> favouritedBy = new HashSet<>();

    @ManyToMany(mappedBy = "purchasedProducts")
    private Set<User> buyers = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId", updatable = false, insertable = false)
    private Set<Feedback> feedbacks = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date dateCreated;

    @LastModifiedDate
    @Column(insertable = false)
    private Date lastUpdated;

    @PostLoad
    @PostPersist
    @PostUpdate
    public void calculateRateOnLoad() {
        calculateRate();
    }

    @Transient
    public void calculateRate() {
        this.rate = calculateRating(this.feedbacks);
    }

    public void incrementSales(int quantity) {
        this.sales = (this.sales == null ? 0L : this.sales) + quantity;
    }

    public void removeUnitsInStock(int quantity) {
        this.unitsInStock -= quantity;
    }

    public void addBuyer(User user) {
        user.addToPurchasedProducts(this);
        buyers.add(user);
    }
}
