package InsightInflux.flux.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyExchangeService {

    private BigDecimal exchangeRate = new BigDecimal("1.2"); // Default value
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiUrl = "https://api.hnb.hr/tecajn-eur/v3?valuta=USD";

    @PostConstruct
    public void init() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::updateExchangeRate, 0, 1, TimeUnit.HOURS);
    }

    private void updateExchangeRate() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            String rateStr = root.path(0).path("srednji_tecaj").asText().replace(",", ".");
            exchangeRate = new BigDecimal(rateStr).setScale(4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        rateUpdated = true;
    }

    public BigDecimal convertEurToUsd(BigDecimal priceInEur) {
        return priceInEur.multiply(exchangeRate);
    }
    
    private volatile boolean rateUpdated = false;

    public void ensureRateIsUpdated() {
        if (!rateUpdated) {
            updateExchangeRate();
        }
    }
}