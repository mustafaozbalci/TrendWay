package commercial.TrendWay.service;

import commercial.TrendWay.dto.CartItemDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Cart;
import commercial.TrendWay.entity.CartItem;
import commercial.TrendWay.entity.Product;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.CartItemRepository;
import commercial.TrendWay.repository.CartRepository;
import commercial.TrendWay.repository.ProductRepository;
import commercial.TrendWay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Adds a product to the cart.
     *
     * @param cartItemDTO DTO containing userId, productId, and quantity.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> addToCart(CartItemDTO cartItemDTO) {
        logger.info("Adding product to cart for user ID: {}", cartItemDTO.getUserId());
        User user = userRepository.findById(cartItemDTO.getUserId()).orElseThrow(() -> new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND));
        Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> new BadRequestException("Product not found", ErrorCodes.PRODUCT_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());

        cartItemRepository.save(cartItem);

        logger.info("Product added to cart for user ID: {}", cartItemDTO.getUserId());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Product added to cart", cart));
    }

    /**
     * Removes a product from the cart.
     *
     * @param cartItemDTO DTO containing userId and productId.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> removeFromCart(CartItemDTO cartItemDTO) {
        logger.info("Removing product from cart for user ID: {}", cartItemDTO.getUserId());
        User user = userRepository.findById(cartItemDTO.getUserId()).orElseThrow(() -> new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND));
        Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> new BadRequestException("Product not found", ErrorCodes.PRODUCT_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new BadRequestException("Cart not found", ErrorCodes.CART_NOT_FOUND));
        List<CartItem> cartItems = cartItemRepository.findByCartAndProduct(cart, product);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart item not found", ErrorCodes.CART_ITEM_NOT_FOUND);
        }

        cartItemRepository.deleteAll(cartItems);
        logger.info("Product removed from cart for user ID: {}", cartItemDTO.getUserId());
        return ResponseEntity.ok(new ResponseModel(200, "Product removed from cart", cart));
    }

    /**
     * Retrieves the cart for a given user.
     *
     * @param userId ID of the user.
     * @return ResponseEntity with ResponseModel containing the cart.
     */
    public ResponseEntity<ResponseModel> getCart(Long userId) {
        logger.info("Retrieving cart for user ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new BadRequestException("Cart not found", ErrorCodes.CART_NOT_FOUND));

        logger.info("Cart retrieved for user ID: {}", userId);
        return ResponseEntity.ok(new ResponseModel(200, "Cart retrieved", cart));
    }
}
