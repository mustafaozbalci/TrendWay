package commercial.TrendWay.controller;

import commercial.TrendWay.dto.PaymentDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<ResponseModel> processPayment(@RequestBody PaymentDTO paymentDTO) {
        logger.info("Request to process payment for order ID: {}", paymentDTO.getOrderId());
        return paymentService.processPayment(paymentDTO);
    }
}
