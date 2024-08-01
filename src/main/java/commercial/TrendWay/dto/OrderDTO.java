package commercial.TrendWay.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long userId;
    private Date orderDate;
    private List<OrderItemDTO> orderItems;
}
