package commercial.TrendWay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private Long walletId;
    private Long userId;
}
