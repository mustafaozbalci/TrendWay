package commercial.TrendWay.service;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.*;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;

    /**
     * Creates an order for a user.
     *
     * @param userId   The ID of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return ResponseEntity with the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> createOrder(Long userId, String username, String password) {
        logger.info("Creating order for user ID: {}", userId);

        User user = userService.validateUser(username, password);
        Cart cart = cartService.getCartByUser(user);
        List<CartItem> cartItems = cartService.getCartItems(cart);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty", ErrorCodes.CART_EMPTY);
        }

        double totalAmount = 0;
        Company company = cartItems.get(0).getProduct().getCompany();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                logger.warn("Insufficient stock for product: {}", product.getName());
                throw new BadRequestException("Insufficient stock for product: " + product.getName(), ErrorCodes.PRODUCT_NOT_FOUND);
            }
            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        Order order = new Order(user, company, null, new Date(), totalAmount);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productService.updateProductStock(product);

            return new OrderItem(order, product, product.getPrice(), cartItem.getQuantity());
        }).toList();

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        cartService.clearCartItems(cartItems);

        logger.info("Order created for user ID: {}", userId);
        return ResponseEntity.status(201).body(new ResponseModel(201, "Order created successfully", order));
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId The ID of the order.
     * @return The order entity.
     * @throws BadRequestException if the order is not found.
     */
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new BadRequestException("Order not found", ErrorCodes.ORDER_NOT_FOUND));
    }
}
