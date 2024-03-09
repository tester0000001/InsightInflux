package InsightInflux.flux.controller;

import InsightInflux.flux.model.Product;
import InsightInflux.flux.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {
        if (code != null) {
            return ResponseEntity.ok(productService.findByCodeContainingIgnoreCase(code));
        } else if (name != null) {
            return ResponseEntity.ok(productService.findByNameContainingIgnoreCase(name));
        } else {
            return ResponseEntity.ok(productService.findAllProducts());
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Product>> getPopularProducts() {
        return ResponseEntity.ok(productService.findPopularProducts());
    }
}