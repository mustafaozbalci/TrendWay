package commercial.TrendWay;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.dto.UserDTO;
import commercial.TrendWay.entity.RoleType;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.UserRepository;
import commercial.TrendWay.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPass");
        userDTO.setEmail("test@test.com");
        userDTO.setFullName("Test User");
        userDTO.setAddress("Test Address");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setRoleName(RoleType.USER);

        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        user.setEmail("test@test.com");
        user.setFullName("Test User");
        user.setAddress("Test Address");
        user.setPhoneNumber("1234567890");
        user.setRole(RoleType.USER);
    }

    @Test
    void registerUser_UserAlreadyExists() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(new User()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.registerUser(userDTO));

        assertEquals("400 User already exists", exception.getMessage());
        assertEquals(ErrorCodes.USER_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    void validateUser_UserNotFound() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());

        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.validateUser(username, password);
        });

        assertEquals("400 User not found", exception.getMessage());
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void validateUser_InvalidCredentials() {
        UserDTO invalidUserDTO = new UserDTO();
        invalidUserDTO.setUsername("testUser");
        invalidUserDTO.setPassword("correctPass");

        User foundUser = new User();
        foundUser.setUsername(invalidUserDTO.getUsername());
        foundUser.setPassword("correctPass");

        when(userRepository.findByUsername(invalidUserDTO.getUsername())).thenReturn(Optional.of(foundUser));

        String username = invalidUserDTO.getUsername();
        String wrongPassword = "wrongPass";
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.validateUser(username, wrongPassword);
        });

        assertEquals("400 Invalid credentials", exception.getMessage());
        assertEquals(ErrorCodes.INVALID_CREDENTIALS, exception.getErrorCode());
    }



    @Test
    void registerUser_Success() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<ResponseModel> responseEntity = userService.registerUser(userDTO);
        ResponseModel responseModel = responseEntity.getBody();

        assertNotNull(responseModel);
        assertEquals("User registered successfully", responseModel.getMessage());
        User returnedUser = (User) responseModel.getData();
        assertEquals(userDTO.getUsername(), returnedUser.getUsername());
    }
}
