package commercial.TrendWay.service;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Category;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    /**
     * Adds a new category.
     *
     * @param category Category entity.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> addCategory(Category category) {
        logger.info("Adding category: {}", category.getName());
        Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory.isPresent()) {
            logger.warn("Category already exists: {}", category.getName());
            throw new BadRequestException("Category already exists", ErrorCodes.CATEGORY_ALREADY_EXISTS);
        }

        category = categoryRepository.save(category);
        logger.info("Category added: {}", category.getName());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Category added successfully", category));
    }

    /**
     * Updates an existing category.
     *
     * @param category Category entity with updated details.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> updateCategory(Category category) {
        logger.info("Updating category ID: {}", category.getId());
        Optional<Category> existingCategory = categoryRepository.findById(category.getId());
        if (existingCategory.isEmpty()) {
            logger.warn("Category not found: {}", category.getId());
            throw new BadRequestException("Category not found", ErrorCodes.CATEGORY_NOT_FOUND);
        }

        Category updatedCategory = existingCategory.get();
        updatedCategory.setName(category.getName());

        categoryRepository.save(updatedCategory);
        logger.info("Category updated ID: {}", category.getId());
        return ResponseEntity.ok(new ResponseModel(200, "Category updated successfully", updatedCategory));
    }
}
