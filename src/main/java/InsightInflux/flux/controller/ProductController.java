package InsightInflux.flux.controller;

import InsightInflux.flux.model.Product;
import InsightInflux.flux.dto.PopularProductDto;
import InsightInflux.flux.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Adds a new product to the catalog and calculates its USD price based on the current exchange rate.")
    @ApiResponse(responseCode = "200", description = "Product successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    @ApiResponse(responseCode = "400", description = "Invalid product data provided")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    @Operation(summary = "Get all products or filter by code/name",
            description = "Retrieves a list of products, optionally filtered by product code or name.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)))
    @ApiResponse(responseCode = "204", description = "No products found")
    public ResponseEntity<List<Product>> getAllProducts(
            @Parameter(description = "Product code to filter by", example = "123ABC")
            @RequestParam(required = false) String code,
            @Parameter(description = "Product name to filter by", example = "Product X")
            @RequestParam(required = false) String name) {

        return ResponseEntity.ok(productService.findProducts(code, name));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates the details of an existing product by ID.")
    @ApiResponse(responseCode = "200", description = "Product successfully updated",
            content = @Content(schema = @Schema(implementation = Product.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "400", description = "Invalid product details provided")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {

        return ResponseEntity.ok(productService.updateProduct(id, productDetails));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.")
    @ApiResponse(responseCode = "200", description = "Product successfully deleted",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProduct(id);
        Map<String, Boolean> response = Map.of("deleted", isDeleted);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/popular")
    @Operation(summary = "List popular products", description = "Retrieves a list of popular products based on predefined criteria.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved popular products",
            content = @Content(schema = @Schema(implementation = PopularProductDto.class)))
    @ApiResponse(responseCode = "204", description = "No popular products found")
    public ResponseEntity<List<PopularProductDto>> getPopularProducts() {

        return ResponseEntity.ok(productService.findPopularProducts());
    }
}