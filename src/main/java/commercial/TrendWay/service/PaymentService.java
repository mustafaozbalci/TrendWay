package commercial.TrendWay.service;

import commercial.TrendWay.client.WalletClient;
import commercial.TrendWay.dto.PaymentDTO;
import commercial.TrendWay.dto.PaymentRequest;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Order;
import commercial.TrendWay.entity.Payment;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.OrderRepository;
import commercial.TrendWay.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final WalletClient walletClient;

    /**
     * Processes a payment for the specified order.
     * Validates order and company existence.
     * Sends a payment request to the Wallet service.
     * Records the payment in the database if successful.
     *
     * @param paymentDTO DTO containing the order ID for which payment is to be processed.
     * @return ResponseEntity with the result of the payment process.
     */
    @Transactional
    public ResponseEntity<ResponseModel> processPayment(PaymentDTO paymentDTO) {
        logger.info("Processing payment for order ID: {}", paymentDTO.getOrderId());
        Order order = orderRepository.findById(paymentDTO.getOrderId()).orElseThrow(() -> {
            logger.warn("Order not found: {}", paymentDTO.getOrderId());
            return new BadRequestException("Order not found", ErrorCodes.ORDER_NOT_FOUND);
        });

        if (order.getCompany() == null) {
            logger.warn("Order does not have an associated company");
            throw new BadRequestException("Order does not have an associated company", ErrorCodes.COMPANY_NOT_FOUND);
        }

        double amount = order.getTotalAmount(); // Siparişin toplam tutarını alın
        String payerUsername = order.getUser().getUsername();
        String payerPassword = order.getUser().getPassword();
        Long payeeUserId = order.getCompany().getWalletId();

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPayerUsername(payerUsername);
        paymentRequest.setPayerPassword(payerPassword);
        paymentRequest.setPayeeUserId(payeeUserId);
        paymentRequest.setAmount(amount);

        ResponseEntity<ResponseModel> paymentResponse = walletClient.makePayment(paymentRequest);

        if (!paymentResponse.getStatusCode().is2xxSuccessful()) {
            logger.error("Payment failed for order ID: {}", paymentDTO.getOrderId());
            throw new BadRequestException("Payment failed", ErrorCodes.INSUFFICIENT_BALANCE);
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPaymentDate(new Date());
        payment.setStatus("Completed");

        paymentRepository.save(payment);

        logger.info("Payment processed for order ID: {}", paymentDTO.getOrderId());
        return paymentResponse;
    }
}
