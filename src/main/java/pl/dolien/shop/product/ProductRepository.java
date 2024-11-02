package pl.dolien.shop.product;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    @Nonnull
    @Query("SELECT p FROM Product p")
    Page<Product> findAll(@Nonnull Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);
}
