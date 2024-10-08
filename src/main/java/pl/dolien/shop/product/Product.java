package pl.dolien.shop.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.user.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private ProductCategory category;

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

    @ManyToMany(mappedBy = "favourites")
    @JsonIgnore
    private List<User> usersWhoFavourited;

    @ManyToMany(mappedBy = "purchasedProducts")
    @JsonIgnore
    private List<User> usersWhoPurchased;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Feedback> feedbacks;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date dateCreated;

    @LastModifiedDate
    @Column(insertable = false)
    private Date lastUpdated;

    @Transient
    public void calculateRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            this.rate = 0.0;
        } else {
            var rate = this.feedbacks.stream()
                    .mapToDouble(Feedback::getNote)
                    .average()
                    .orElse(0.0);

            this.rate = Math.round(rate * 10.0) / 10.0;
        }
    }

    public void addSales(int quantity) {
        if(this.sales == null || this.sales == 0) {
            this.sales = (long) quantity;
        } else {
            this.sales = this.sales + quantity;
        }
    }

    public void removeUnitsInStock(int quantity) {
        this.unitsInStock = this.unitsInStock - quantity;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void calculateRateOnLoad() {
        calculateRate();
    }

    public void addUserWhoPurchased(User user) {
        if (usersWhoPurchased == null) {
            usersWhoPurchased = new ArrayList<>();
        }
        usersWhoPurchased.add(user);
    }
}
