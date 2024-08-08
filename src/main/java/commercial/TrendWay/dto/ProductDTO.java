package commercial.TrendWay.dto;

import commercial.TrendWay.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private double price;
    private int stock;
    private Long companyId;
    private List<Category> categories;
}
