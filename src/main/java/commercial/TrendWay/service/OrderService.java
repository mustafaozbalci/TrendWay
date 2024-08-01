package commercial.TrendWay.service;

import commercial.TrendWay.dto.OrderDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Order;
import commercial.TrendWay.entity.OrderItem;
import commercial.TrendWay.entity.Product;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.OrderRepository;
import commercial.TrendWay.repository.ProductRepository;
import commercial.TrendWay.repository.UserRepository;
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

    /**
     * Creates a new order for the given user.
     *
     * @param orderDTO DTO containing userId and order items.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> createOrder(OrderDTO orderDTO) {
        logger.info("Creating order for user ID: {}", orderDTO.getUserId());
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND));

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(itemDTO -> {
            Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(() -> new BadRequestException("Product not found", ErrorCodes.PRODUCT_NOT_FOUND));
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName(), ErrorCodes.PRODUCT_NOT_FOUND);
            }
            product.setStock(product.getStock() - itemDTO.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());
            return orderItem;
        }).collect(Collectors.toList());

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setOrderDate(new Date());

        orderRepository.save(order);

        logger.info("Order created for user ID: {}", orderDTO.getUserId());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Order created successfully", order));
    }
}
