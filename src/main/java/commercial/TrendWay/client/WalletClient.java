package commercial.TrendWay.client;

import commercial.TrendWay.dto.PaymentRequest;
import commercial.TrendWay.dto.ResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WalletClient {

    private final RestTemplate restTemplate;
    private final String walletServiceUrl;

    public WalletClient(RestTemplate restTemplate, @Value("${wallet.service.url}") String walletServiceUrl) {
        this.restTemplate = restTemplate;
        this.walletServiceUrl = walletServiceUrl;
    }

    public ResponseEntity<ResponseModel> makePayment(PaymentRequest paymentRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("payerUsername", paymentRequest.getPayerUsername());
        headers.set("payerPassword", paymentRequest.getPayerPassword());

        HttpEntity<PaymentRequest> entity = new HttpEntity<>(new PaymentRequest(null, null, paymentRequest.getPayeeWalletId(), paymentRequest.getAmount()), headers);
        try {
            return restTemplate.exchange(walletServiceUrl + "/wallets/make-payment", HttpMethod.POST, entity, ResponseModel.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(500, "Payment service error: " + e.getMessage(), null));
        }
    }
}
