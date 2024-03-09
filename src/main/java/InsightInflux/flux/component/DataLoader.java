package InsightInflux.flux.component;

import InsightInflux.flux.model.Product;
import InsightInflux.flux.model.Review;
import InsightInflux.flux.repository.ProductRepository;
import InsightInflux.flux.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostConstruct
    private void loadData() {
        // Predefined products
        List<Product> products = List.of(
                new Product("123456789012341", "Product 1", new BigDecimal("100.00"), new BigDecimal("110.00"), "Description of Product 1"),
                new Product("123456789012342", "Product 2", new BigDecimal("200.00"), new BigDecimal("220.00"), "Description of Product 1"),
                new Product("123456789012343", "Product 3", new BigDecimal("300.00"), new BigDecimal("330.00"), "Description of Product 1"),
                new Product("123456789012344", "Product 4", new BigDecimal("400.00"), new BigDecimal("440.00"), "Description of Product 1"),
                new Product("123456789012345", "Product 5", new BigDecimal("500.00"), new BigDecimal("550.00"), "Description of Product 1")
        );

        products.forEach(productRepository::save);

        // Predefined reviews for each product
        List<Review> reviews = new ArrayList<>();
        products.forEach(product -> {
            reviews.add(new Review(product, "Reviewer 1", "Great product", 5));
            reviews.add(new Review(product, "Reviewer 2", "Satisfactory", 4));
        });
        reviews.forEach(reviewRepository::save);
    }
}