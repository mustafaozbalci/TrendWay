package commercial.TrendWay.client;

import commercial.TrendWay.dto.PaymentRequest;
import commercial.TrendWay.dto.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WalletClient {

    private static final Logger logger = LoggerFactory.getLogger(WalletClient.class);
    private final RestTemplate restTemplate;

    public ResponseEntity<ResponseModel> makePayment(PaymentRequest paymentRequest) {
        logger.info("Sending payment request to Wallet: {}", paymentRequest);
        String url = "http://localhost:8081/wallets/make-payment";
        ResponseEntity<ResponseModel> response = null;
        try {
            response = restTemplate.postForEntity(url, paymentRequest, ResponseModel.class);
        } catch (Exception e) {
            logger.error("Error while making payment request: {}", e.getMessage());
            throw e;
        }
        logger.info("Payment response: {}", response);
        return response;
    }
}
