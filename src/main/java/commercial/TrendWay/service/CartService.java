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
     * Adds a product to the user's cart. If the product is already in the cart, it updates the quantity.
     */
    @Transactional
    public ResponseEntity<ResponseModel> addToCart(String payerUsername, String payerPassword, CartItemDTO cartItemDTO) {
        logger.info("Adding product to cart for user ID: {}", cartItemDTO.getUserId());

        // Find user by ID
        User user = userRepository.findById(cartItemDTO.getUserId()).orElseThrow(() -> {
            logger.warn("User not found: {}", cartItemDTO.getUserId());
            return new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND);
        });

        // Validate user credentials
        if (!user.getUsername().equals(payerUsername) || !user.getPassword().equals(payerPassword)) {
            logger.warn("User authentication failed for user ID: {}", cartItemDTO.getUserId());
            return ResponseEntity.status(403).body(new ResponseModel(403, "User authentication failed", null));
        }

        // Find product by ID
        Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> {
            logger.warn("Product not found: {}", cartItemDTO.getProductId());
            return new BadRequestException("Product not found", ErrorCodes.PRODUCT_NOT_FOUND);
        });

        // Find or create cart for user
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        // Check if product is already in the cart
        CartItem existingCartItem = cartItemRepository.findByCartAndProduct(cart, product).stream().findFirst().orElse(null);

        // Update quantity if product exists, otherwise add new cart item
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDTO.getQuantity());
            cartItemRepository.save(cartItem);
        }

        logger.info("Product added to cart for user ID: {}", cartItemDTO.getUserId());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Product added to cart", cart));
    }

    /**
     * Removes a product from the user's cart.
     */
    @Transactional
    public ResponseEntity<ResponseModel> removeFromCart(String payerUsername, String payerPassword, CartItemDTO cartItemDTO) {
        logger.info("Removing product from cart for user ID: {}", cartItemDTO.getUserId());

        // Find user by ID
        User user = userRepository.findById(cartItemDTO.getUserId()).orElseThrow(() -> {
            logger.warn("User not found: {}", cartItemDTO.getUserId());
            return new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND);
        });

        // Validate user credentials
        if (!user.getUsername().equals(payerUsername) || !user.getPassword().equals(payerPassword)) {
            logger.warn("User authentication failed for user ID: {}", cartItemDTO.getUserId());
            return ResponseEntity.status(403).body(new ResponseModel(403, "User authentication failed", null));
        }

        // Find product by ID
        Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(() -> {
            logger.warn("Product not found: {}", cartItemDTO.getProductId());
            return new BadRequestException("Product not found", ErrorCodes.PRODUCT_NOT_FOUND);
        });

        // Find cart for user
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> {
            logger.warn("Cart not found for user ID: {}", cartItemDTO.getUserId());
            return new BadRequestException("Cart not found", ErrorCodes.CART_NOT_FOUND);
        });

        // Find cart items
        List<CartItem> cartItems = cartItemRepository.findByCartAndProduct(cart, product);

        // Check if cart item exists
        if (cartItems.isEmpty()) {
            logger.warn("Cart item not found for product ID: {}", cartItemDTO.getProductId());
            throw new BadRequestException("Cart item not found", ErrorCodes.CART_ITEM_NOT_FOUND);
        }

        // Remove cart items
        cartItemRepository.deleteAll(cartItems);
        logger.info("Product removed from cart for user ID: {}", cartItemDTO.getUserId());
        return ResponseEntity.ok(new ResponseModel(200, "Product removed from cart", cart));
    }

    /**
     * Retrieves the user's cart.
     */
    public ResponseEntity<ResponseModel> getCart(String payerUsername, String payerPassword, Long userId) {
        logger.info("Retrieving cart for user ID: {}", userId);

        // Find user by ID
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("User not found: {}", userId);
            return new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND);
        });

        // Validate user credentials
        if (!user.getUsername().equals(payerUsername) || !user.getPassword().equals(payerPassword)) {
            logger.warn("User authentication failed for user ID: {}", userId);
            return ResponseEntity.status(403).body(new ResponseModel(403, "User authentication failed", null));
        }

        // Find cart for user
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> {
            logger.warn("Cart not found for user ID: {}", userId);
            return new BadRequestException("Cart not found", ErrorCodes.CART_NOT_FOUND);
        });

        logger.info("Cart retrieved for user ID: {}", userId);
        return ResponseEntity.ok(new ResponseModel(200, "Cart retrieved", cart));
    }
}
