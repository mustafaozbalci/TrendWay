package commercial.TrendWay.service;

import commercial.TrendWay.dto.ProductDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Category;
import commercial.TrendWay.entity.Company;
import commercial.TrendWay.entity.Product;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CompanyService companyService;
    private final UserService userService;

    /**
     * Adds a product.
     *
     * @param adminUsername The username of the admin.
     * @param adminPassword The password of the admin.
     * @param productDTO    The product data transfer object containing product details.
     * @return ResponseEntity with the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> addProduct(String adminUsername, String adminPassword, ProductDTO productDTO) {
        logger.info("Adding product: {}", productDTO.getName());

        User admin = userService.validateUser(adminUsername, adminPassword);

        Company company = companyService.getCompanyById(productDTO.getCompanyId());

        if (!company.getUser().equals(admin)) {
            logger.warn("User {} does not own the company {}", adminUsername, productDTO.getCompanyId());
            throw new BadRequestException("User does not own the company", ErrorCodes.INVALID_CREDENTIALS);
        }

        Set<Category> categories = productDTO.getCategories().stream().collect(Collectors.toSet());

        Optional<Product> existingProductOpt = productRepository.findByNameAndCompany(productDTO.getName(), company);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setStock(existingProduct.getStock() + productDTO.getStock());
            productRepository.save(existingProduct);
            logger.info("Product stock updated: {}", existingProduct.getName());
            return ResponseEntity.status(200).body(new ResponseModel(200, "Product stock updated successfully", existingProduct));
        }

        Product product = new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getStock(), company, categories);
        product = productRepository.save(product);

        logger.info("Product added: {}", product.getName());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Product added successfully", product));
    }

    /**
     * Lists all products.
     *
     * @return ResponseEntity with the list of all products.
     */
    public ResponseEntity<ResponseModel> listProducts() {
        logger.info("Listing all products");
        return ResponseEntity.ok(new ResponseModel(200, "Products retrieved successfully", productRepository.findAll()));
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product.
     * @return The product entity.
     * @throws BadRequestException if the product is not found.
     */
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new BadRequestException("Product not found", ErrorCodes.PRODUCT_NOT_FOUND));
    }

    /**
     * Updates the stock of a product.
     *
     * @param product The product whose stock is to be updated.
     */
    public void updateProductStock(Product product) {
        productRepository.save(product);
    }
}
