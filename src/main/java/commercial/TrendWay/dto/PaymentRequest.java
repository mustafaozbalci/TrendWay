package commercial.TrendWay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String payerUsername;
    private String payerPassword;
    private Long payeeWalletId;
    private Double amount;
}
