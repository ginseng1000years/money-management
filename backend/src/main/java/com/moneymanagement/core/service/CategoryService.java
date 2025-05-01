package com.moneymanagement.core.service;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.model.Category;
import com.moneymanagement.core.repository.CategoryRepository;
import com.moneymanagement.core.validator.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing categories.
 * Provides methods to retrieve all categories and add a new category.
 * Validates incoming category data before saving.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all categories from the repository and maps them to DTOs.
     *
     * @return List of CategoryDTO representing all categories.
     */
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Validates and adds a new category to the repository.
     * Maps the incoming CategoryDTO to a Category entity, validates it,
     * saves it, and returns the saved entity as a CategoryDTO.
     *
     * @param categoryDTO The CategoryDTO containing data to add.
     * @return The saved Category mapped to a CategoryDTO.
     * @throws IllegalArgumentException if validation fails.
     */
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        // Validation
        CategoryValidator.validate(categoryDTO);

        // Map DTO to entity
        Category category = categoryDTO.toEntity();

        Category savedCategory = categoryRepository.save(category);
        return CategoryDTO.fromEntity(savedCategory);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to delete.
     */
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Updates a category by its ID.
     *
     * @param id The ID of the category to update.
     * @param categoryDTO The CategoryDTO containing updated data.
     * @return The updated CategoryDTO.
     * @throws IllegalArgumentException if validation fails or category not found.
     */
    public CategoryDTO updateCategory(String id, CategoryDTO categoryDTO) {
        // Validate input
        CategoryValidator.validate(categoryDTO);

        // Find existing category
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        // Update fields
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setType(categoryDTO.getType());
        existingCategory.setImage(categoryDTO.getImage());
        existingCategory.setDescription(categoryDTO.getDescription());

        if (categoryDTO.getParentCategory() != null && categoryDTO.getParentCategory().getId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentCategory().getId())
                    .orElse(null);
            existingCategory.setParentCategory(parentCategory);
        } else {
            existingCategory.setParentCategory(null);
        }

        // Save updated category
        Category updatedCategory = categoryRepository.save(existingCategory);

        return CategoryDTO.fromEntity(updatedCategory);
    }
}
