package commercial.TrendWay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    private Long userId;
    private Long orderId;
    private String paymentMethod;
    private double amount;
}
