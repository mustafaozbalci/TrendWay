package commercial.TrendWay.dto;

import commercial.TrendWay.entity.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private RoleType roleName;
    private String cart;
}