package InsightInflux.flux.controller;

import InsightInflux.flux.model.Review;
import InsightInflux.flux.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        try {
            Review createdReview = reviewService.createReview(review);
            return ResponseEntity.ok(createdReview);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviewsByProductId(@RequestParam(required = false) Long productId) {
        if (productId != null) {
            return ResponseEntity.ok(reviewService.findByProductId(productId));
        } else {
            return ResponseEntity.badRequest().body(List.of()); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable(value = "id") Long reviewId,
                                            @RequestBody Review reviewDetails) {
        Review updatedReview = reviewService.updateReview(reviewId, reviewDetails);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteReview(@PathVariable(value = "id") Long reviewId) {
        reviewService.deleteReview(reviewId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}