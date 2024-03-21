package InsightInflux.flux.service;

import InsightInflux.flux.dto.ExchangeRateDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public CurrencyExchangeService() {
    }

    @PostConstruct
    public void init() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::updateExchangeRate, 0, 1, TimeUnit.HOURS);
    }

    private void updateExchangeRate() {
        try {
            var response = restTemplate.getForObject(apiUrl, String.class);
            JsonNode root = objectMapper.readTree(response);
            var rateStr = root.path(0).path("srednji_tecaj").asText().replace(",", ".");
            exchangeRate = new BigDecimal(rateStr).setScale(4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExchangeRateDto getExchangeRateDto() {
        ensureRateIsUpdated();
        return new ExchangeRateDto(exchangeRate, "USD");
    }

    private volatile boolean rateUpdated = false;

    private void ensureRateIsUpdated() {
        if (!rateUpdated) {
            updateExchangeRate();
        }
    }
}