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
    private final UserService userService;
    private final ProductService productService;

    /**
     * Adds a product to the cart.
     *
     * @param payerUsername The username of the payer.
     * @param payerPassword The password of the payer.
     * @param cartItemDTO   The cart item data transfer object containing product and quantity details.
     * @return ResponseEntity with the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> addToCart(String payerUsername, String payerPassword, CartItemDTO cartItemDTO) {
        logger.info("Adding product to cart for user ID: {}", cartItemDTO.getUserId());

        User user = userService.validateUser(payerUsername, payerPassword);
        Product product = productService.getProductById(cartItemDTO.getProductId());

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> cartRepository.save(new Cart(user)));

        CartItem existingCartItem = cartItemRepository.findByCartAndProduct(cart, product).stream().findFirst().orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem(cart, product, cartItemDTO.getQuantity());
            cartItemRepository.save(cartItem);
        }

        logger.info("Product added to cart for user ID: {}", cartItemDTO.getUserId());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Product added to cart", cart));
    }

    /**
     * Removes a product from the cart.
     *
     * @param payerUsername The username of the payer.
     * @param payerPassword The password of the payer.
     * @param cartItemDTO   The cart item data transfer object containing product and quantity details.
     * @return ResponseEntity with the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> removeFromCart(String payerUsername, String payerPassword, CartItemDTO cartItemDTO) {
        logger.info("Removing product from cart for user ID: {}", cartItemDTO.getUserId());

        User user = userService.validateUser(payerUsername, payerPassword);
        Product product = productService.getProductById(cartItemDTO.getProductId());
        Cart cart = findCartByUserOrThrow(user);

        List<CartItem> cartItems = cartItemRepository.findByCartAndProduct(cart, product);
        if (cartItems.isEmpty()) {
            logger.warn("Cart item not found for product ID: {}", cartItemDTO.getProductId());
            throw new BadRequestException("Cart item not found", ErrorCodes.CART_ITEM_NOT_FOUND);
        }

        cartItemRepository.deleteAll(cartItems);
        logger.info("Product removed from cart for user ID: {}", cartItemDTO.getUserId());
        return ResponseEntity.ok(new ResponseModel(200, "Product removed from cart", cart));
    }

    /**
     * Retrieves the cart for a given user.
     *
     * @param payerUsername The username of the payer.
     * @param payerPassword The password of the payer.
     * @param userId        The ID of the user whose cart is to be retrieved.
     * @return ResponseEntity with the cart details.
     */
    public ResponseEntity<ResponseModel> getCart(String payerUsername, String payerPassword, Long userId) {
        logger.info("Retrieving cart for user ID: {}", userId);

        User user = userService.validateUser(payerUsername, payerPassword);
        Cart cart = findCartByUserOrThrow(user);

        logger.info("Cart retrieved for user ID: {}", userId);
        return ResponseEntity.ok(new ResponseModel(200, "Cart retrieved", cart));
    }

    /**
     * Gets the cart for a given user.
     *
     * @param user The user whose cart is to be retrieved.
     * @return The cart of the user.
     */
    public Cart getCartByUser(User user) {
        return findCartByUserOrThrow(user);
    }

    /**
     * Gets the items in a given cart.
     *
     * @param cart The cart whose items are to be retrieved.
     * @return A list of cart items.
     */
    public List<CartItem> getCartItems(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }

    /**
     * Clears the items in a given cart.
     *
     * @param cartItems The list of cart items to be cleared.
     */
    public void clearCartItems(List<CartItem> cartItems) {
        cartItemRepository.deleteAll(cartItems);
    }

    /**
     * Finds the cart for a given user or throws an exception if not found.
     *
     * @param user The user whose cart is to be retrieved.
     * @return The cart of the user.
     * @throws BadRequestException if the cart is not found.
     */
    private Cart findCartByUserOrThrow(User user) {
        return cartRepository.findByUser(user).orElseThrow(() -> {
            logger.warn("Cart not found for user ID: {}", user.getId());
            return new BadRequestException("Cart not found", ErrorCodes.CART_NOT_FOUND);
        });
    }
}
