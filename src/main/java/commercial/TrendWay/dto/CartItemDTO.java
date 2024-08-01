package commercial.TrendWay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
