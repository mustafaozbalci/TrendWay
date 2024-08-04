package commercial.TrendWay.controller;

import commercial.TrendWay.dto.OrderDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ResponseModel> createOrder(@RequestBody OrderDTO orderDTO) {
        if (orderDTO.getUserId() == null) {
            return ResponseEntity.badRequest().body(new ResponseModel(400, "User ID cannot be null", null));
        }
        return orderService.createOrder(orderDTO.getUserId());
    }
}
