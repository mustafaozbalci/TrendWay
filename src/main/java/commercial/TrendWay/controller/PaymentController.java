package commercial.TrendWay.controller;

import commercial.TrendWay.dto.PaymentDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<ResponseModel> processPayment(@RequestHeader("payerUsername") String payerUsername, @RequestHeader("payerPassword") String payerPassword, @RequestBody PaymentDTO paymentDTO) {
        logger.info("Request to process payment for order ID: {}", paymentDTO.getOrderId());
        ResponseEntity<ResponseModel> response = paymentService.processPayment(paymentDTO.getOrderId(), payerUsername, payerPassword);

        if (response == null || response.getBody() == null) {
            logger.error("Payment response or response body is null for order ID: {}", paymentDTO.getOrderId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(500, "Payment processing failed", null));
        }

        logger.info("Payment response: {}", response.getBody().getMessage());
        return response;
    }
}
