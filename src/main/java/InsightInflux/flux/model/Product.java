package InsightInflux.flux.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_eur", nullable = false, precision = 15, scale = 2)
    private BigDecimal priceEur;

    @Column(name = "price_usd", precision = 15, scale = 2)
    private BigDecimal priceUsd;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;
}
