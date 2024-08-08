package commercial.TrendWay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    private Set<Category> categories;
}
