package commercial.TrendWay.service;

import commercial.TrendWay.client.WalletClient;
import commercial.TrendWay.dto.PaymentRequest;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final OrderService orderService;
    private final WalletClient walletClient;

    public PaymentService(OrderService orderService, WalletClient walletClient) {
        this.orderService = orderService;
        this.walletClient = walletClient;
    }

    /**
     * Processes a payment for a given order.
     *
     * @param orderId       The ID of the order.
     * @param payerUsername The username of the payer.
     * @param payerPassword The password of the payer.
     * @return ResponseEntity with the result of the payment processing.
     */
    public ResponseEntity<ResponseModel> processPayment(Long orderId, String payerUsername, String payerPassword) {
        Order order = orderService.getOrderById(orderId);
        double totalAmount = order.getTotalAmount();
        Long payeeWalletId = order.getCompany().getWalletId();

        logger.info("Order ID: {}, Total Amount: {}, Payee Wallet ID: {}", orderId, totalAmount, payeeWalletId);

        if (payeeWalletId == null) {
            logger.warn("Payee Wallet ID is null for order ID: {}", orderId);
            return ResponseEntity.badRequest().body(new ResponseModel(400, "Payee Wallet ID is null", null));
        }

        PaymentRequest paymentRequest = new PaymentRequest(payerUsername, payerPassword, payeeWalletId, totalAmount);
        logger.info("Sending payment request to Wallet service: {}", paymentRequest);
        ResponseEntity<ResponseModel> paymentResponse = walletClient.makePayment(paymentRequest);

        if (paymentResponse.getBody() == null) {
            logger.error("Payment response body is null for order ID: {}", orderId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(500, "Payment processing failed", null));
        }

        return paymentResponse;
    }
}
