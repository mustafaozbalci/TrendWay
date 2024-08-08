package commercial.TrendWay.controller;

import commercial.TrendWay.dto.CartItemDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.CartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addToCart(@RequestHeader("payerUsername") String payerUsername, @RequestHeader("payerPassword") String payerPassword, @RequestBody CartItemDTO cartItemDTO) {
        logger.info("Request to add product to cart for user ID: {}", cartItemDTO.getUserId());
        return cartService.addToCart(payerUsername, payerPassword, cartItemDTO);
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseModel> removeFromCart(@RequestHeader("payerUsername") String payerUsername, @RequestHeader("payerPassword") String payerPassword, @RequestBody CartItemDTO cartItemDTO) {
        logger.info("Request to remove product from cart for user ID: {}", cartItemDTO.getUserId());
        return cartService.removeFromCart(payerUsername, payerPassword, cartItemDTO);
    }

    @PostMapping("/view")
    public ResponseEntity<ResponseModel> getCart(@RequestHeader("payerUsername") String payerUsername, @RequestHeader("payerPassword") String payerPassword, @RequestBody CartItemDTO cartItemDTO) {
        logger.info("Request to view cart for user ID: {}", cartItemDTO.getUserId());
        return cartService.getCart(payerUsername, payerPassword, cartItemDTO.getUserId());
    }
}
