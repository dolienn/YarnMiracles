package pl.dolien.shop.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.role.Role;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;
    private LocalDate dateOfBirth;

    private boolean accountLocked;
    private boolean enabled;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "favourite_products",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favourites = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "purchased_products",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> purchasedProducts = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "createdBy", updatable = false, insertable = false)
    private Set<Feedback> feedbacks = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roles == null) {
            return new HashSet<>();
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    public void addToPurchasedProducts(Product product) {
        if (product == null) {
            return;
        }
        purchasedProducts.add(product);
    }

    public void addToRoles(Role role) {
        if (role == null) {
            return;
        }

        if (this.roles == null) {
            this.roles = new HashSet<>();
        }

        roles.add(role);
    }

    public void removeFromRoles(Role role) {
        if (role == null) {
            return;
        }

        if (this.roles == null) {
            this.roles = new HashSet<>();
        }

        roles.remove(role);
    }
}
