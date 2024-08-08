package commercial.TrendWay.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
}
