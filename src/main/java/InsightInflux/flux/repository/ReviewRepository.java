package InsightInflux.flux.repository;

import InsightInflux.flux.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct_Id(Long productId);
    List<Review> findByRating(Integer rating);

    @Query("SELECT r FROM Review r WHERE r.product.name LIKE %:name%")
    List<Review> findByProductName(@Param("name") String name);
}