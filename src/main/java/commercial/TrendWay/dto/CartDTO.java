package commercial.TrendWay.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Long userId;
    private List<CartItemDTO> items;
}
