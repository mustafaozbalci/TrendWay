package commercial.TrendWay.service;

import commercial.TrendWay.dto.ProductDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Category;
import commercial.TrendWay.entity.Company;
import commercial.TrendWay.entity.Product;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.CategoryRepository;
import commercial.TrendWay.repository.CompanyRepository;
import commercial.TrendWay.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Adds a new product.
     *
     * @param productDTO DTO containing product details.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> addProduct(ProductDTO productDTO) {
        logger.info("Adding product: {}", productDTO.getName());
        Optional<Company> companyOpt = companyRepository.findByName(productDTO.getCompanyName());
        if (companyOpt.isEmpty()) {
            throw new BadRequestException("Company not found", ErrorCodes.COMPANY_NOT_FOUND);
        }

        Company company = companyOpt.get();
        List<Category> categories = productDTO.getCategoryNames().stream().map(name -> categoryRepository.findByName(name).orElseThrow(() -> new BadRequestException("Category not found: " + name, ErrorCodes.CATEGORY_NOT_FOUND))).collect(Collectors.toList());

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
     * @return ResponseEntity with ResponseModel containing the list of products.
     */
    public ResponseEntity<ResponseModel> listProducts() {
        logger.info("Listing all products");
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(new ResponseModel(200, "Products retrieved successfully", products));
    }
}
