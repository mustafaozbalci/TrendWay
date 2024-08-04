package commercial.TrendWay.service;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.dto.UserDTO;
import commercial.TrendWay.entity.Cart;
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
     * @param userDTO DTO containing user information.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> registerUser(UserDTO userDTO) {
        logger.info("Registering user: {}", userDTO.getUsername());
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new BadRequestException("User already exists", ErrorCodes.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(userDTO.getRoleName());
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        user = userRepository.save(user);
        logger.info("User registered: {}", user.getUsername());
        return ResponseEntity.status(201).body(new ResponseModel(201, "User registered successfully", user));
    }
}
