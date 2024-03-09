package InsightInflux.flux.service;

import InsightInflux.flux.model.Review;
import InsightInflux.flux.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @SuppressWarnings("null")
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
