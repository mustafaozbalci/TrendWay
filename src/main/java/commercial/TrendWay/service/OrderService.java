package commercial.TrendWay.service;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.*;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * Creates an order for the specified user.
     * Validates user existence and cart contents.
     * Decrements product stock and creates order items.
     *
     * @param userId ID of the user creating the order.
     * @return ResponseEntity with the result of the order creation process.
     */
    @Transactional
    public ResponseEntity<ResponseModel> createOrder(Long userId) {
        logger.info("Creating order for user ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new BadRequestException("Cart not found", ErrorCodes.CART_NOT_FOUND));
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty", ErrorCodes.CART_EMPTY);
        }

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName(), ErrorCodes.PRODUCT_NOT_FOUND);
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            return orderItem;
        }).collect(Collectors.toList());

        Order order = new Order();
        order.setUser(user);
        order.setCompany(cartItems.get(0).getProduct().getCompany()); // Şirket bilgisini buradan ayarlıyoruz
        order.setOrderItems(orderItems);
        order.setOrderDate(new Date());

        orderRepository.save(order);

        logger.info("Order created for user ID: {}", userId);
        return ResponseEntity.status(201).body(new ResponseModel(201, "Order created successfully", order));
    }
}
