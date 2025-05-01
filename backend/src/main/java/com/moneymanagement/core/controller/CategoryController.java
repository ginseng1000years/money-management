package com.moneymanagement.core.controller;

import com.moneymanagement.core.model.Category;
import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * REST controller for managing categories.
 * Provides endpoints to retrieve all categories and add a new category.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Retrieves a list of all categories.
     *
     * @return List of CategoryDTO objects representing all categories.
     */
    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * Adds a new category.
     *
     * @param categoryDTO The CategoryDTO object containing category details to add.
     * @return The added CategoryDTO object.
     */
    @PostMapping
    public CategoryDTO addCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }

    /**
     * Updates a category by its ID.
     *
     * @param id The ID of the category to update.
     * @param categoryDTO The CategoryDTO object containing updated category details.
     * @return The updated CategoryDTO object.
     */
    @PutMapping("/{id}")
    public CategoryDTO updateCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
    }
}
