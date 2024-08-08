package commercial.TrendWay.service;

import commercial.TrendWay.client.WalletClient;
import commercial.TrendWay.dto.PaymentRequest;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Order;
import commercial.TrendWay.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final OrderRepository orderRepository;
    private final WalletClient walletClient;

    public PaymentService(OrderRepository orderRepository, WalletClient walletClient) {
        this.orderRepository = orderRepository;
        this.walletClient = walletClient;
    }

    /**
     * Processes a payment for a given order.
     *
     * @param orderId       The ID of the order to process payment for.
     * @param payerUsername The username of the payer.
     * @param payerPassword The password of the payer.
     * @return ResponseEntity with the payment processing status and details.
     */
    public ResponseEntity<ResponseModel> processPayment(Long orderId, String payerUsername, String payerPassword) {
        // Find the order by ID
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            logger.warn("Order not found with ID: {}", orderId);
            return ResponseEntity.badRequest().body(new ResponseModel(400, "Order not found", null));
        }

        Order order = optionalOrder.get();
        double totalAmount = order.getTotalAmount();
        Long payeeWalletId = order.getCompany().getWalletId();

        logger.info("Order ID: {}, Total Amount: {}, Payee Wallet ID: {}", orderId, totalAmount, payeeWalletId);

        // Check if the payee wallet ID is null
        if (payeeWalletId == null) {
            logger.warn("Payee Wallet ID is null for order ID: {}", orderId);
            return ResponseEntity.badRequest().body(new ResponseModel(400, "Payee Wallet ID is null", null));
        }

        // Create payment request
        PaymentRequest paymentRequest = new PaymentRequest(payerUsername, payerPassword, payeeWalletId, totalAmount);
        logger.info("Sending payment request to Wallet service: {}", paymentRequest);
        ResponseEntity<ResponseModel> paymentResponse = walletClient.makePayment(paymentRequest);

        // Check if the payment response body is null
        if (paymentResponse.getBody() == null) {
            logger.error("Payment response body is null for order ID: {}", orderId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(500, "Payment processing failed", null));
        }

        return paymentResponse;
    }
}
