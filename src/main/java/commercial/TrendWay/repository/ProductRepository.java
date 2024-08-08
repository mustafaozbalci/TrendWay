package commercial.TrendWay.repository;

import commercial.TrendWay.entity.Company;
import commercial.TrendWay.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndCompany(String name, Company company);
}
