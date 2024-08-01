package commercial.TrendWay.controller;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.dto.UserDTO;
import commercial.TrendWay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseModel> registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }
}
