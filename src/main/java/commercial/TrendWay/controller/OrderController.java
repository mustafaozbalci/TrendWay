package commercial.TrendWay.controller;

import commercial.TrendWay.dto.OrderDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ResponseModel> createOrder(@RequestHeader("payerUsername") String payerUsername, @RequestHeader("payerPassword") String payerPassword, @RequestBody OrderDTO orderDTO) {
        logger.info("Request to create order for user ID: {}", orderDTO.getUserId());
        return orderService.createOrder(orderDTO.getUserId(), payerUsername, payerPassword);
    }
}
