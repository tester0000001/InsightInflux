package InsightInflux.flux.controller;

import InsightInflux.flux.model.Review;
import InsightInflux.flux.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
}