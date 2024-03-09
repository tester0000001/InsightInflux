package InsightInflux.flux.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeService {

    public BigDecimal convertEurToUsd(BigDecimal priceInEur) {
    
        BigDecimal exchangeRate = new BigDecimal("1.2"); // Placeholder value
        return priceInEur.multiply(exchangeRate);
    }
}