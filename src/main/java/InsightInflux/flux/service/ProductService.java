package InsightInflux.flux.service;

import InsightInflux.flux.dto.ExchangeRateDto;
import InsightInflux.flux.dto.PopularProductDto;
import InsightInflux.flux.dto.ProductDTO;
import InsightInflux.flux.dto.ReviewDTO;
import InsightInflux.flux.model.Product;
import InsightInflux.flux.model.Review;
import InsightInflux.flux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    public ProductDTO createProduct(ProductDTO productDTO) {
        ExchangeRateDto exchangeRate = currencyExchangeService.getExchangeRateDto();
        BigDecimal priceInUsd = productDTO.getPriceEur().multiply(exchangeRate.getRate()).setScale(2, RoundingMode.HALF_UP);
        productDTO.setPriceUsd(priceInUsd);

        Product product = convertToProductEntity(productDTO);
        product = productRepository.save(product);
        return convertToProductDTO(product);
    }

    public List<ProductDTO> findProducts(String code, String name) {
        List<Product> products;
        if (code != null && !code.trim().isEmpty()) {
            products = productRepository.findByCodeContainingIgnoreCase(code);
        } else if (name != null && !name.trim().isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(name);
        } else {
            products = productRepository.findAll();
        }
        return products.stream().map(this::convertToProductDTO).collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for this id :: " + id));

        updateProductEntityWithDTO(product, productDTO);
        product = productRepository.save(product);
        return convertToProductDTO(product);
    }

    public Map<String, Boolean> deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for this id :: " + id));

        productRepository.delete(product);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return response;
    }

    public List<PopularProductDto> findPopularProducts() {
        return productRepository.findTopPopularProducts(PageRequest.of(0, 3));
    }

    private ProductDTO convertToProductDTO(Product product) {
        List<ReviewDTO> reviewDTOs = product.getReviews().stream()
                .map(review -> new ReviewDTO(
                        review.getId(),
                        review.getProduct().getId(),
                        review.getReviewer(),
                        review.getText(),
                        review.getRating()))
                .collect(Collectors.toList());

        return new ProductDTO(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getPriceEur(),
                product.getPriceUsd(),
                product.getDescription(),
                reviewDTOs
        );
    }

    private Product convertToProductEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setPriceEur(dto.getPriceEur());
        product.setPriceUsd(dto.getPriceUsd());
        product.setDescription(dto.getDescription());
        if (dto.getReviews() != null) {
            product.setReviews(dto.getReviews().stream().map(this::convertToReviewEntity).collect(Collectors.toList()));
        }
        return product;
    }

    private void updateProductEntityWithDTO(Product product, ProductDTO dto) {
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setPriceEur(dto.getPriceEur());
        product.setDescription(dto.getDescription());
        ExchangeRateDto exchangeRate = currencyExchangeService.getExchangeRateDto();
        product.setPriceUsd(dto.getPriceEur().multiply(exchangeRate.getRate()).setScale(2, RoundingMode.HALF_UP));
    }

    private Review convertToReviewEntity(ReviewDTO dto) {
        Review review = new Review();
        review.setId(dto.getId());
        review.setReviewer(dto.getReviewer());
        review.setText(dto.getText());
        review.setRating(dto.getRating());
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            review.setProduct(product);
        }
        return review;
    }

    public Product createAndSaveProduct(String code, String name, BigDecimal priceEur, String description) {
        ExchangeRateDto exchangeRateDto = currencyExchangeService.getExchangeRateDto();
        BigDecimal priceInUsd = priceEur.multiply(exchangeRateDto.getRate()).setScale(2, RoundingMode.HALF_UP);

        Product product = Product.builder()
                .code(code)
                .name(name)
                .priceEur(priceEur)
                .priceUsd(priceInUsd)
                .description(description)
                .build();

        return productRepository.save(product);
    }
}
