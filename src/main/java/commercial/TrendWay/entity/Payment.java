package commercial.TrendWay.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Date paymentDate;

    @Column(nullable = false)
    private String status;

    public Payment(Order order, Double amount, Date paymentDate, String status) {
        this.order = order;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }
}