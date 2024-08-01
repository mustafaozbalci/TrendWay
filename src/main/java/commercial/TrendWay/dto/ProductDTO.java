package commercial.TrendWay.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String companyName;
    private List<String> categoryNames;
}
