package commercial.TrendWay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {
    private Long productId;
    private int quantity;
    private double price;
}
