package commercial.TrendWay.controller;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Category;
import commercial.TrendWay.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseModel> updateCategory(@RequestBody Category categoryDetails) {
        return categoryService.updateCategory(categoryDetails);
    }
}