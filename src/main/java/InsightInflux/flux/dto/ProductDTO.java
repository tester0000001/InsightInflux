package InsightInflux.flux.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    
    private Long id;
    private String code;
    private String name;
    private BigDecimal priceEur;
    private BigDecimal priceUsd;
    private String description;
    private List<ReviewDTO> reviews;
}