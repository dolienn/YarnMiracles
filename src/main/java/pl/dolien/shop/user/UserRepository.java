package pl.dolien.shop.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dolien.shop.product.Product;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.favourites FROM User u WHERE u.id = :userId")
    Page<Product> findFavouritesByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT p FROM User u JOIN u.favourites p WHERE u.id = :userId ORDER BY p.unitPrice ASC")
    Page<Product> findFavouritesByUserIdOrderAsc(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT p FROM User u JOIN u.favourites p WHERE u.id = :userId ORDER BY p.unitPrice DESC")
    Page<Product> findFavouritesByUserIdOrderDesc(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT p FROM User u JOIN u.favourites p WHERE u.id = :userId ORDER BY p.rate DESC")
    Page<Product> findFavouritesByUserIdOrderByRateDesc(@Param("userId") Integer userId, Pageable pageable);
}
