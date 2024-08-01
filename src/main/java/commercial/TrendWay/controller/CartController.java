package commercial.TrendWay.controller;

import commercial.TrendWay.dto.CartItemDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        return cartService.addToCart(cartItemDTO);
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseModel> removeFromCart(@RequestBody CartItemDTO cartItemDTO) {
        return cartService.removeFromCart(cartItemDTO);
    }

    @PostMapping("/view")
    public ResponseEntity<ResponseModel> getCart(@RequestBody CartItemDTO cartItemDTO) {
        return cartService.getCart(cartItemDTO.getUserId());
    }

}
