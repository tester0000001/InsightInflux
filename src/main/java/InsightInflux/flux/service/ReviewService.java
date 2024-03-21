package InsightInflux.flux.service;

import InsightInflux.flux.model.Product;
import InsightInflux.flux.model.Review;
import InsightInflux.flux.repository.ProductRepository;
import InsightInflux.flux.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public ResponseEntity<Review> createReview(Review review) {
        try {
            Long productId = review.getProductId();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found for this id: " + productId));
            review.setProduct(product);
            Review createdReview = reviewRepository.save(review);

            return ResponseEntity.ok(createdReview);
        } catch (EntityNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<Review>> findByProductId(Long productId) {
        try {
            if (!productRepository.existsById(productId)) {
                throw new EntityNotFoundException("Product not found for this id: " + productId);
            }
            List<Review> reviews = reviewRepository.findByProduct_Id(productId);

            return ResponseEntity.ok(reviews);

        } catch (EntityNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Review> updateReview(Long id, Review reviewDetails) {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Review not found for this id: " + id));
            if (reviewDetails.getProductId() != null && !productRepository.existsById(reviewDetails.getProductId())) {
                throw new EntityNotFoundException("Product not found for this id: " + reviewDetails.getProductId());
            }
            review.setReviewer(reviewDetails.getReviewer());
            review.setText(reviewDetails.getText());
            review.setRating(reviewDetails.getRating());
            Review updatedReview = reviewRepository.save(review);

            return ResponseEntity.ok(updatedReview);

        } catch (EntityNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Map<String, Boolean>> deleteReview(Long id) {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Review not found for this id: " + id));
            reviewRepository.delete(review);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", true);

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }

    public void addReview(Product product, String reviewerName, String text, int rating) {
        Review review = Review.builder()
                .product(product)
                .reviewer(reviewerName)
                .text(text)
                .rating(rating)
                .build();

        reviewRepository.save(review);
    }
}
