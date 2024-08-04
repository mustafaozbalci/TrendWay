package commercial.TrendWay.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String email;
    private String phoneNumber;

    @Column(name = "wallet_id")
    private Long walletId;
}
