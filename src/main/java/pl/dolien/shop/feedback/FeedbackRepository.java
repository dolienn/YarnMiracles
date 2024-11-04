package pl.dolien.shop.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("""
            SELECT f
            FROM Feedback f
            WHERE f.product.id = :productId
            """)
    Page<Feedback> findAllByProductId(@Param("productId") Long productId, Pageable pageable);
}
