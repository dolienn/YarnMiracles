package pl.dolien.shop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);

    Page<Product> findByCategoryIdOrderByUnitPriceAsc(@Param("id") Long id, Pageable pageable);

    Page<Product> findByCategoryIdOrderByUnitPriceDesc(@Param("id") Long id, Pageable pageable);

    Page<Product> findByCategoryIdOrderByRateDesc(@Param("id") Long id, Pageable pageable);

    Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);

    Page<Product> findByNameContainingOrderByUnitPriceAsc(@Param("name") String name, Pageable pageable);

    Page<Product> findByNameContainingOrderByUnitPriceDesc(@Param("name") String name, Pageable pageable);

    Page<Product> findByNameContainingOrderByRateDesc(@Param("name") String name, Pageable pageable);
}
