package commercial.TrendWay.service;

import commercial.TrendWay.dto.ProductDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Category;
import commercial.TrendWay.entity.Company;
import commercial.TrendWay.entity.Product;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.CompanyRepository;
import commercial.TrendWay.repository.ProductRepository;
import commercial.TrendWay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    /**
     * Adds a new product or updates the stock of an existing product for a company.
     *
     * @param adminUsername the username of the admin
     * @param adminPassword the password of the admin
     * @param productDTO    the product details
     * @return ResponseEntity with the result of the operation
     */
    @Transactional
    public ResponseEntity<ResponseModel> addProduct(String adminUsername, String adminPassword, ProductDTO productDTO) {
        logger.info("Adding product: {}", productDTO.getName());

        // Authenticate admin user
        Optional<User> userOpt = userRepository.findByUsername(adminUsername);
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(adminPassword)) {
            logger.warn("Invalid credentials for username: {}", adminUsername);
            throw new BadRequestException("Invalid credentials", ErrorCodes.INVALID_CREDENTIALS);
        }

        User user = userOpt.get();

        // Check if company exists and belongs to the admin
        Optional<Company> companyOpt = companyRepository.findById(productDTO.getCompanyId());
        if (companyOpt.isEmpty()) {
            logger.warn("Company not found: {}", productDTO.getCompanyId());
            throw new BadRequestException("Company not found", ErrorCodes.COMPANY_NOT_FOUND);
        }

        Company company = companyOpt.get();
        if (!company.getUser().equals(user)) {
            logger.warn("User {} does not own the company {}", adminUsername, productDTO.getCompanyId());
            throw new BadRequestException("User does not own the company", ErrorCodes.INVALID_CREDENTIALS);
        }

        Set<Category> categories = productDTO.getCategories().stream().collect(Collectors.toSet());

        // Check if product already exists for the company
        Optional<Product> existingProductOpt = productRepository.findByNameAndCompany(productDTO.getName(), company);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setStock(existingProduct.getStock() + productDTO.getStock());
            productRepository.save(existingProduct);
            logger.info("Product stock updated: {}", existingProduct.getName());
            return ResponseEntity.status(200).body(new ResponseModel(200, "Product stock updated successfully", existingProduct));
        }

        // Create new product if it does not exist
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCompany(company);
        product.setCategories(categories);

        product = productRepository.save(product);

        logger.info("Product added: {}", product.getName());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Product added successfully", product));
    }

    /**
     * Lists all products.
     *
     * @return ResponseEntity with the list of all products
     */
    public ResponseEntity<ResponseModel> listProducts() {
        logger.info("Listing all products");
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(new ResponseModel(200, "Products retrieved successfully", products));
    }
}
