package InsightInflux.flux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    
    private Long id;
    private Long productId;
    private String reviewer;
    private String text;
    private Integer rating;
}
