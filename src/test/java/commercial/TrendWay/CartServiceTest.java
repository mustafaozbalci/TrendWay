package commercial.TrendWay;

import commercial.TrendWay.dto.CartItemDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.*;
import commercial.TrendWay.repository.CartItemRepository;
import commercial.TrendWay.repository.CartRepository;
import commercial.TrendWay.service.CartService;
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
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private Cart cart;
    private CartItem cartItem;
    private CartItemDTO cartItemDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User("testUser", "testPass", "test@test.com", "Test User", "Test Address", "1234567890", RoleType.USER);
        product = new Product("Test Product", "Test Description", 10.0, 100, null, Collections.emptySet());
        cart = new Cart(user);
        cartItem = new CartItem(cart, product, 1);
        cartItemDTO = new CartItemDTO();
        cartItemDTO.setUserId(user.getId());
        cartItemDTO.setProductId(product.getId());
        cartItemDTO.setQuantity(1);
    }

    @Test
    void addToCart_Success() {
        when(userService.validateUser(user.getUsername(), user.getPassword())).thenReturn(user);
        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Collections.singletonList(cartItem));

        ResponseEntity<ResponseModel> response = cartService.addToCart(user.getUsername(), user.getPassword(), cartItemDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getData());
        assertEquals("Product added to cart", response.getBody().getMessage());
    }

    @Test
    void removeFromCart_Success() {
        when(userService.validateUser(user.getUsername(), user.getPassword())).thenReturn(user);
        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Collections.singletonList(cartItem));

        ResponseEntity<ResponseModel> response = cartService.removeFromCart(user.getUsername(), user.getPassword(), cartItemDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getData());
        assertEquals("Product removed from cart", response.getBody().getMessage());
    }

    @Test
    void getCart_Success() {
        when(userService.validateUser(user.getUsername(), user.getPassword())).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        ResponseEntity<ResponseModel> response = cartService.getCart(user.getUsername(), user.getPassword(), user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getData());
        assertEquals("Cart retrieved", response.getBody().getMessage());
    }
}
