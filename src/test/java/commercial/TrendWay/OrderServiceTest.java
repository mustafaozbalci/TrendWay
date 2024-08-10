package commercial.TrendWay;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.*;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.OrderRepository;
import commercial.TrendWay.service.CartService;
import commercial.TrendWay.service.OrderService;
import commercial.TrendWay.service.ProductService;
import commercial.TrendWay.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Cart cart;
    private Company company;
    private Product product;
    private CartItem cartItem;
    private Order order;
    private List<CartItem> cartItems;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User("testUser", "testPass", "test@test.com", "Test User", "Test Address", "1234567890", RoleType.USER);
        company = new Company("Test Company", "Test Address", "test@company.com", "1234567890", 1L, user);
        product = new Product("Test Product", "Test Description", 10.0, 100, company, Collections.emptySet());
        cart = new Cart(user);
        cartItem = new CartItem(cart, product, 1);
        cartItems = List.of(cartItem);
        order = new Order(user, company, cartItems.stream().map(item -> new OrderItem(order, product, product.getPrice(), item.getQuantity())).toList(), new Date(), 10.0);
    }

    @Test
    void createOrder_Success() {
        when(userService.validateUser(user.getUsername(), user.getPassword())).thenReturn(user);
        when(cartService.getCartByUser(user)).thenReturn(cart);
        when(cartService.getCartItems(cart)).thenReturn(cartItems);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        ResponseEntity<ResponseModel> response = orderService.createOrder(user.getId(), user.getUsername(), user.getPassword());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getData());
        assertEquals("Order created successfully", response.getBody().getMessage());
    }

    @Test
    void createOrder_CartEmpty() {
        when(userService.validateUser(user.getUsername(), user.getPassword())).thenReturn(user);
        when(cartService.getCartByUser(user)).thenReturn(cart);
        when(cartService.getCartItems(cart)).thenReturn(Collections.emptyList());
        Long userId = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            orderService.createOrder(userId, username, password);
        });

        assertEquals("400 Cart is empty", exception.getMessage());
        assertEquals(ErrorCodes.CART_EMPTY.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void createOrder_InsufficientStock() {
        product.setStock(0);
        when(userService.validateUser(user.getUsername(), user.getPassword())).thenReturn(user);
        when(cartService.getCartByUser(user)).thenReturn(cart);
        when(cartService.getCartItems(cart)).thenReturn(cartItems);
        Long userId = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            orderService.createOrder(userId, username, password);
        });

        assertEquals("400 Insufficient stock for product: Test Product", exception.getMessage());
        assertEquals(ErrorCodes.PRODUCT_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }


    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(order.getId());

        assertNotNull(foundOrder);
        assertEquals(order.getId(), foundOrder.getId());
    }

    @Test
    void getOrderById_NotFound() {
        Long orderId = order.getId();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            orderService.getOrderById(orderId);
        });

        assertEquals("400 Order not found", exception.getMessage());
        assertEquals(ErrorCodes.ORDER_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }

}
