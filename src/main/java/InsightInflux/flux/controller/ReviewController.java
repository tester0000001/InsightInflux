package InsightInflux.flux.controller;

import InsightInflux.flux.model.Review;
import InsightInflux.flux.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviewsByProductId(@RequestParam(required = false) Long productId) {
        if (productId != null) {
            return ResponseEntity.ok(reviewService.findByProductId(productId));
        } else {
            // when productId is not provided
            return ResponseEntity.badRequest().body(List.of()); 
        }
    }
}