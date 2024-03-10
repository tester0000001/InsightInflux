package InsightInflux.flux.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    @Column(name = "reviewer", nullable = false)
    private String reviewer;

    @Column(name = "text", nullable = false)
    @Size(max = 500, message = "Review text must be less than 500 characters")
    private String text;

    @Column(name = "rating", nullable = false)
    @NotNull(message = "Rating is required")
    @Min(1)
    @Max(5)
    private Integer rating;

    // Transient productId for JSON mapping
    @Transient
    private Long productId;

    // Default constructor
    public Review() {
    }

    // Constructor
    public Review(Product product, String reviewer, String text, Integer rating) {
        this.product = product;
        this.reviewer = reviewer;
        this.text = text;
        this.rating = rating;
    }

    // Standard getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
