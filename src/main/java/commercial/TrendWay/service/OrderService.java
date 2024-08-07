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

    @Transactional
    public ResponseEntity<ResponseModel> createOrder(Long userId) {
        logger.info("Creating order for user ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("User not found: {}", userId);
            return new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND);
        });
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> {
            logger.warn("Cart not found for user ID: {}", userId);
            return new BadRequestException("Cart not found", ErrorCodes.CART_NOT_FOUND);
        });
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty", ErrorCodes.CART_EMPTY);
        }

        double totalAmount = 0;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                logger.warn("Insufficient stock for product: {}", product.getName());
                throw new BadRequestException("Insufficient stock for product: " + product.getName(), ErrorCodes.PRODUCT_NOT_FOUND);
            }
            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        // Order nesnesini burada tanımlıyoruz
        Order order = new Order();
        order.setUser(user);
        order.setCompany(cartItems.get(0).getProduct().getCompany());
        order.setOrderDate(new Date());
        order.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        logger.info("Order created for user ID: {}", userId);
        return ResponseEntity.status(201).body(new ResponseModel(201, "Order created successfully", order));
    }
}
