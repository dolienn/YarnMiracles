package pl.dolien.shop.product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dolien.shop.exception.NotEnoughStockException;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.user.User;

import java.math.BigDecimal;
import java.util.*;

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

    private Integer categoryId;

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

    @ManyToMany(mappedBy = "favourites", fetch = FetchType.LAZY)
    private Set<User> favouritedByUsers = new HashSet<>();

    @ManyToMany(mappedBy = "purchasedProducts", fetch = FetchType.LAZY)
    private Set<User> buyers = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "productId", updatable = false, insertable = false)
    private List<Feedback> feedbacks = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date dateCreated;

    @LastModifiedDate
    @Column(insertable = false)
    private Date lastUpdated;

    @Transient
    public void calculateRate() {
        this.rate = RatingCalculator.calculateRate(this.feedbacks);
    }

    public void incrementSales(int quantity) {
        this.sales = (this.sales == null ? 0L : this.sales) + quantity;
    }

    public void removeUnitsInStock(int quantity) {
        if (this.unitsInStock >= quantity) {
            this.unitsInStock -= quantity;
        } else {
            throw new NotEnoughStockException("Not enough units in stock");
        }
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void calculateRateOnLoad() {
        calculateRate();
    }

    public void addUserWhoPurchased(User user) {
        user.addToPurchasedProducts(this);
        buyers.add(user);
    }
}
