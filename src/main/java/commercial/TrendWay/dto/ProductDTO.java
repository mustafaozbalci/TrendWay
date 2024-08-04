package commercial.TrendWay.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private double price;
    private int stock;
    private Long companyId;
    private List<Long> categoryIds;
}
