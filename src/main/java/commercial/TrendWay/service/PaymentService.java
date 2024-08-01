package commercial.TrendWay.service;

import commercial.TrendWay.dto.PaymentDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Order;
import commercial.TrendWay.entity.Payment;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.OrderRepository;
import commercial.TrendWay.repository.PaymentRepository;
import commercial.TrendWay.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    /**
     * Processes the payment for the given order.
     *
     * @param paymentDTO DTO containing userId, orderId, payment method, and amount.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> processPayment(PaymentDTO paymentDTO) {
        logger.info("Processing payment for user ID: {}", paymentDTO.getUserId());
        User user = userRepository.findById(paymentDTO.getUserId()).orElseThrow(() -> new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND));

        Order order = orderRepository.findById(paymentDTO.getOrderId()).orElseThrow(() -> new BadRequestException("Order not found", ErrorCodes.ORDER_NOT_FOUND));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentDate(new Date());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setStatus("Completed");

        paymentRepository.save(payment);

        logger.info("Payment processed for user ID: {}", paymentDTO.getUserId());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Payment processed successfully", payment));
    }
}
