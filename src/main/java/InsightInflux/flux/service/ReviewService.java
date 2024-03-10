package InsightInflux.flux.service;

import InsightInflux.flux.model.Product;
import InsightInflux.flux.model.Review;
import InsightInflux.flux.repository.ReviewRepository;
import InsightInflux.flux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;



@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public Review createReview(Review review) {
        if(review.getProductId() != null) {
            Optional<Product> product = productRepository.findById(review.getProductId());
            product.ifPresent(review::setProduct); 
        }
        return reviewRepository.save(review);
    }

    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProduct_Id(productId);
    }

    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review not found for this id :: " + id));
        
        review.setReviewer(reviewDetails.getReviewer());
        review.setText(reviewDetails.getText());
        review.setRating(reviewDetails.getRating());
    
        return reviewRepository.save(review);
    }
    
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review not found for this id :: " + id));
    
        reviewRepository.delete(review);
    }
}
