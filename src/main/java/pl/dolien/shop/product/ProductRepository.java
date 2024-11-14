package pl.dolien.shop.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p")
    List<Product> findAllProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.categoryId = :id")
    List<Product> findByCategoryId(@Param("id") Integer id, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContaining(@Param("name") String name, Pageable pageable);
}
