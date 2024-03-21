package InsightInflux.flux.service;

import InsightInflux.flux.dto.ExchangeRateDto;
import InsightInflux.flux.model.Product;
import InsightInflux.flux.dto.PopularProductDto;
import InsightInflux.flux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    public Product createProduct(Product product) {
        ExchangeRateDto exchangeRateDto = currencyExchangeService.getExchangeRateDto();
        BigDecimal priceInUsd = product.getPriceEur().multiply(exchangeRateDto.getRate()).setScale(2, RoundingMode.HALF_UP);
        product.setPriceUsd(priceInUsd);

        return productRepository.save(product);
    }

    public List<Product> findByCodeContainingIgnoreCase(String code) {

        return productRepository.findByCodeContainingIgnoreCase(code);
    }

    public List<Product> findByNameContainingIgnoreCase(String name) {

        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findProducts(String code, String name) {
        if (code != null && !code.trim().isEmpty()) {

            return findByCodeContainingIgnoreCase(code);

        } else if (name != null && !name.trim().isEmpty()) {

            return findByNameContainingIgnoreCase(name);

        } else {

            return findAllProducts();
        }
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for this id :: " + id));

        product.setCode(productDetails.getCode());
        product.setName(productDetails.getName());
        product.setPriceEur(productDetails.getPriceEur());
        product.setDescription(productDetails.getDescription());

        ExchangeRateDto exchangeRateDto = currencyExchangeService.getExchangeRateDto();
        BigDecimal priceInUsd = productDetails.getPriceEur().multiply(exchangeRateDto.getRate()).setScale(2, RoundingMode.HALF_UP);
        product.setPriceUsd(priceInUsd);

        return productRepository.save(product);
    }


    public boolean deleteProduct(Long id) {

        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);

                    return true;
                }).orElse(false);
    }

    public List<PopularProductDto> findPopularProducts() {

        return productRepository.findTopPopularProducts(PageRequest.of(0, 3));
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