package InsightInflux.flux.controller;

import InsightInflux.flux.dto.ProductDTO;
import InsightInflux.flux.dto.PopularProductDto;
import InsightInflux.flux.service.ProductService;
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
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Adds a new product to the catalog and calculates its USD price based on the current exchange rate.")
    @ApiResponse(responseCode = "200", description = "Product successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid product data provided")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping
    @Operation(summary = "Get all products or filter by code/name", description = "Retrieves a list of products, optionally filtered by product code or name.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)))
    @ApiResponse(responseCode = "204", description = "No products found")
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {
        List<ProductDTO> products = productService.findProducts(code, name);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates the details of an existing product by ID.")
    @ApiResponse(responseCode = "200", description = "Product successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "400", description = "Invalid product details provided")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.")
    @ApiResponse(responseCode = "200", description = "Product successfully deleted", content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable Long id) {
        Map<String, Boolean> response = productService.deleteProduct(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/popular")
    @Operation(summary = "List popular products", description = "Retrieves a list of popular products based on predefined criteria.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved popular products", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PopularProductDto.class)))
    @ApiResponse(responseCode = "204", description = "No popular products found")
    public ResponseEntity<List<PopularProductDto>> getPopularProducts() {
        List<PopularProductDto> popularProducts = productService.findPopularProducts();
        return ResponseEntity.ok(popularProducts);
    }
}
