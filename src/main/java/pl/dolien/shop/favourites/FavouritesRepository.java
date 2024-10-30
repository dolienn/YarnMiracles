package pl.dolien.shop.favourites;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.user.User;

@Repository
public interface FavouritesRepository extends JpaRepository<User, Integer> {

    @Query("SELECT p FROM User u JOIN u.favourites p WHERE u.id = :userId")
    Page<Product> findFavouritesByUserId(@Param("userId") Integer userId, Pageable pageable);
}
