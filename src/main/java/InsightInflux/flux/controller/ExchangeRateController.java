package InsightInflux.flux.controller;

import InsightInflux.flux.dto.ExchangeRateDto;
import InsightInflux.flux.service.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Exchange Rate", description = "The Exchange Rate API")
public class ExchangeRateController {

    private final CurrencyExchangeService currencyExchangeService;

    @Autowired
    public ExchangeRateController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping("/api/exchange-rate")
    @Operation(
            summary = "Get current exchange rate",
            description = "Fetches the current exchange rate from EUR to USD.",
            tags = { "Exchange Rate" }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Exchange rate successfully retrieved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExchangeRateDto.class)
            )
    )
    @ApiResponse(responseCode = "500", description = "Failed to fetch exchange rate")
    public ResponseEntity<ExchangeRateDto> getExchangeRate() {
        ExchangeRateDto exchangeRateDto = currencyExchangeService.getExchangeRateDto();
        return ResponseEntity.ok(exchangeRateDto);
    }
}
