package commercial.TrendWay.service;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.dto.UserDTO;
import commercial.TrendWay.entity.Cart;
import commercial.TrendWay.entity.RoleType;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    /**
     * Registers a new user.
     *
     * @param userDTO the user details
     * @return ResponseEntity with the result of the operation
     */
    @Transactional
    public ResponseEntity<ResponseModel> registerUser(UserDTO userDTO) {
        logger.info("Registering user: {}", userDTO.getUsername());
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            logger.warn("User already exists: {}", userDTO.getUsername());
            throw new BadRequestException("User already exists", ErrorCodes.USER_ALREADY_EXISTS);
        }

        // Create a new user and set its properties
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(userDTO.getRoleName());

        // Create a cart for the user and set the user for the cart
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        // Save the user to the repository
        userRepository.save(user);
        logger.info("User registered: {}", user.getUsername());
        return ResponseEntity.status(201).body(new ResponseModel(201, "User registered successfully", user));
    }

    /**
     * Registers a new admin.
     *
     * @param userDTO the admin details
     * @return ResponseEntity with the result of the operation
     */
    @Transactional
    public ResponseEntity<ResponseModel> registerAdmin(UserDTO userDTO) {
        logger.info("Registering admin: {}", userDTO.getUsername());

        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            logger.warn("User already exists: {}", userDTO.getUsername());
            throw new BadRequestException("User already exists", ErrorCodes.USER_ALREADY_EXISTS);
        }

        // Create a new admin and set its properties
        User admin = new User();
        admin.setUsername(userDTO.getUsername());
        admin.setPassword(userDTO.getPassword());
        admin.setEmail(userDTO.getEmail());
        admin.setFullName(userDTO.getFullName());
        admin.setAddress(userDTO.getAddress());
        admin.setPhoneNumber(userDTO.getPhoneNumber());
        admin.setRole(RoleType.ADMIN);

        // Create a cart for the admin and set the admin for the cart
        Cart cart = new Cart();
        cart.setUser(admin);
        admin.setCart(cart);

        // Save the admin to the repository
        userRepository.save(admin);
        logger.info("Admin registered: {}", admin.getUsername());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Admin registered successfully", admin));
    }
}
