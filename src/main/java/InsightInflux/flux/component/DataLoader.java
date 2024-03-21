package InsightInflux.flux.component;

import InsightInflux.flux.model.Product;
import InsightInflux.flux.service.ProductService;
import InsightInflux.flux.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;

@Component
public class DataLoader {

    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;

    @PostConstruct
    public void loadData() {
        for (int i = 1; i <= 5; i++) {
            String code = String.format("12345678901234%d", i);
            String name = String.format("Product %d", i);
            BigDecimal priceEur = new BigDecimal("100.00").add(new BigDecimal(i * 10));
            String description = String.format("Description of Product %d", i);

            Product product = productService.createAndSaveProduct(code, name, priceEur, description);

            reviewService.addReview(product, "Reviewer 1", "Great product", 5);
            reviewService.addReview(product, "Reviewer 2", "Satisfactory", 4);
        }
    }
}