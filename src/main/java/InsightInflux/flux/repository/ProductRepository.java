package InsightInflux.flux.repository;

import InsightInflux.flux.dto.PopularProduct;
import InsightInflux.flux.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCodeContainingIgnoreCase(String code);
    List<Product> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Product p WHERE p.code LIKE %:searchTerm% OR p.name LIKE %:searchTerm%")
    List<Product> findByCodeOrNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT new InsightInflux.flux.dto.PopularProduct(p.name, AVG(r.rating)) " +
    "FROM Product p JOIN p.reviews r " +
    "GROUP BY p.id " +
    "ORDER BY AVG(r.rating) DESC, COUNT(r) DESC")
    List<PopularProduct> findTopPopularProducts(Pageable pageable);
}