package commercial.TrendWay.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
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

    public Product(String name, String description, double price, int stock, Company company, Set<Category> categories) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.company = company;
        this.categories = categories;
    }
}