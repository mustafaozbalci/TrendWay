package commercial.TrendWay.service;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.dto.UserDTO;
import commercial.TrendWay.entity.Cart;
import commercial.TrendWay.entity.RoleType;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.UserRepository;
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
     * @param userDTO The user data transfer object containing user details.
     * @return ResponseEntity with the result of the operation.
     */
    public ResponseEntity<ResponseModel> registerUser(UserDTO userDTO) {
        logger.info("Registering user: {}", userDTO.getUsername());
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            logger.warn("User already exists: {}", userDTO.getUsername());
            throw new BadRequestException("User already exists", ErrorCodes.USER_ALREADY_EXISTS);
        }

        User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getFullName(), userDTO.getAddress(), userDTO.getPhoneNumber(), userDTO.getRoleName());
        Cart cart = new Cart(user, null);
        user.setCart(cart);

        userRepository.save(user);
        logger.info("User registered: {}", user.getUsername());
        return ResponseEntity.status(201).body(new ResponseModel(201, "User registered successfully", user));
    }

    /**
     * Registers a new admin.
     *
     * @param userDTO The user data transfer object containing user details.
     * @return ResponseEntity with the result of the operation.
     */
    public ResponseEntity<ResponseModel> registerAdmin(UserDTO userDTO) {
        logger.info("Registering admin: {}", userDTO.getUsername());

        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            logger.warn("User already exists: {}", userDTO.getUsername());
            throw new BadRequestException("User already exists", ErrorCodes.USER_ALREADY_EXISTS);
        }

        User admin = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getFullName(), userDTO.getAddress(), userDTO.getPhoneNumber(), RoleType.ADMIN);
        Cart cart = new Cart(admin, null);
        admin.setCart(cart);

        userRepository.save(admin);
        logger.info("Admin registered: {}", admin.getUsername());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Admin registered successfully", admin));
    }

    /**
     * Validates an admin user.
     *
     * @param username The username of the admin.
     * @param password The password of the admin.
     * @return The admin user entity.
     * @throws BadRequestException if the admin user is not found or credentials are invalid.
     */
    public User validateAdminUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException("Admin user not found", ErrorCodes.USER_NOT_FOUND));

        if (!user.getPassword().equals(password) || !user.getRole().equals(RoleType.ADMIN)) {
            throw new BadRequestException("Invalid credentials or not an admin user", ErrorCodes.INVALID_CREDENTIALS);
        }

        return user;
    }

    /**
     * Updates a user.
     *
     * @param user The user to be updated.
     */
    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**
     * Validates a user.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The user entity.
     * @throws BadRequestException if the user is not found or credentials are invalid.
     */
    public User validateUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException("User not found", ErrorCodes.USER_NOT_FOUND));

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials", ErrorCodes.INVALID_CREDENTIALS);
        }

        return user;
    }
}
