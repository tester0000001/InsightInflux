package InsightInflux.flux.controller;

import InsightInflux.flux.model.Product;
import InsightInflux.flux.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}