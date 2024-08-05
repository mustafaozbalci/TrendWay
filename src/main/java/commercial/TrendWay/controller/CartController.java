package commercial.TrendWay.controller;

import commercial.TrendWay.dto.CartItemDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.CartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        logger.info("Request to add product to cart for user ID: {}", cartItemDTO.getUserId());
        return cartService.addToCart(cartItemDTO);
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseModel> removeFromCart(@RequestBody CartItemDTO cartItemDTO) {
        logger.info("Request to remove product from cart for user ID: {}", cartItemDTO.getUserId());
        return cartService.removeFromCart(cartItemDTO);
    }

    @PostMapping("/view")
    public ResponseEntity<ResponseModel> getCart(@RequestBody CartItemDTO cartItemDTO) {
        logger.info("Request to view cart for user ID: {}", cartItemDTO.getUserId());
        return cartService.getCart(cartItemDTO.getUserId());
    }
}
