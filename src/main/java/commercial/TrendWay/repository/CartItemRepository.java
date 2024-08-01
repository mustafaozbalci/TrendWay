package commercial.TrendWay.repository;

import commercial.TrendWay.entity.Cart;
import commercial.TrendWay.entity.CartItem;
import commercial.TrendWay.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartAndProduct(Cart cart, Product product);
}
