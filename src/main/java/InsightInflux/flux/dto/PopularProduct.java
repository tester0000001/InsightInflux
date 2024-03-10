package InsightInflux.flux.dto;

public class PopularProduct {
    private String name;
    private Double averageRating;

    public PopularProduct(String name, Double averageRating) {
        this.name = name;
        this.averageRating = Math.round(averageRating * 10.0) / 10.0;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public Double getAverageRating() {
        return averageRating;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
