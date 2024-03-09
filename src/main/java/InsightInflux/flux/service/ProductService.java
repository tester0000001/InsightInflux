package InsightInflux.flux.service;

import InsightInflux.flux.model.Product;
import InsightInflux.flux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    public Product createProduct(Product product) {
        BigDecimal priceInUsd = currencyExchangeService.convertEurToUsd(product.getPriceEur());
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

    public List<Product> findPopularProducts() {
      
        return null; // placeholder 
    }
}