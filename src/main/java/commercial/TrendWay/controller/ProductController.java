package commercial.TrendWay.controller;

import commercial.TrendWay.dto.ProductDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addProduct(@RequestBody ProductDTO productDTO) {
        logger.info("Request to add product: {}", productDTO.getName());
        return productService.addProduct(productDTO);
    }

    @PostMapping("/list")
    public ResponseEntity<ResponseModel> listProducts() {
        logger.info("Request to list all products");
        return productService.listProducts();
    }
}
