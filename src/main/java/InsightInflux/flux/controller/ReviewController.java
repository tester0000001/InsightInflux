package InsightInflux.flux.controller;

import InsightInflux.flux.model.Review;
import InsightInflux.flux.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Create a review", description = "Creates a new review for a product.")
    @ApiResponse(responseCode = "200", description = "Review successfully created",
            content = @Content(schema = @Schema(implementation = Review.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {

        return reviewService.createReview(review);
    }

    @GetMapping()
    @Operation(summary = "Get all reviews by product ID", description = "Retrieves all reviews for a specific product.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews",
            content = @Content(schema = @Schema(implementation = Review.class)))
    @ApiResponse(responseCode = "400", description = "Invalid product ID provided")
    public ResponseEntity<List<Review>> getAllReviewsByProductId(@RequestParam(required = false) Long productId) {

        return reviewService.findByProductId(productId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a review", description = "Updates the details of an existing review by ID.")
    @ApiResponse(responseCode = "200", description = "Review successfully updated",
            content = @Content(schema = @Schema(implementation = Review.class)))
    @ApiResponse(responseCode = "404", description = "Review not found")
    public ResponseEntity<Review> updateReview(@PathVariable(value = "id") Long reviewId, @RequestBody Review reviewDetails) {

        return reviewService.updateReview(reviewId, reviewDetails);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review", description = "Deletes a review by its ID.")
    @ApiResponse(responseCode = "200", description = "Review successfully deleted",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Review not found")
    public ResponseEntity<Map<String, Boolean>> deleteReview(@PathVariable(value = "id") Long reviewId) {

        return reviewService.deleteReview(reviewId);
    }
}