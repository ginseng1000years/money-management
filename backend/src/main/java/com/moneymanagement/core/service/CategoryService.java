package com.moneymanagement.core.service;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.mapper.CategoryMapper;
import com.moneymanagement.core.model.Category;
import com.moneymanagement.core.repository.CategoryRepository;
import com.moneymanagement.core.validator.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Service class for managing categories.
 * Provides methods to retrieve all categories and add a new category.
 * Validates incoming category data before saving.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidator categoryValidator;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CategoryValidator categoryValidator) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categoryValidator = categoryValidator;
    }

    /**
     * Retrieves a paginated and sorted list of categories as DTOs.
     *
     * @param page the page number to retrieve (zero-based)
     * @param size the number of categories per page
     * @param sort the field by which to sort categories
     * @param direction the sort direction, either "asc" or "desc"
     * @return a page of CategoryDTO objects matching the pagination and sorting criteria
     */
    public Page<CategoryDTO> getAllCategories(int page, int size, String sort, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map(categoryMapper::fromEntity);
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
        categoryValidator.validate(categoryDTO);

        // Map DTO to entity
        Category category = categoryMapper.toEntity(categoryDTO);

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.fromEntity(savedCategory);
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
        categoryValidator.validate(categoryDTO);

        // Find existing category
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        // Update fields using DTO
        categoryMapper.updateEntity(existingCategory, categoryDTO);
        
        // Set parent category if provided
        if (categoryDTO.getParentCategory() != null && categoryDTO.getParentCategory().getId() != null) {
            if (categoryDTO.getParentCategory().getId().equals(id)) {
                throw new IllegalArgumentException("Cannot set category as its own parent");
            } else {
                Category parentCategory = categoryRepository.findById(categoryDTO.getParentCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found with id: " + categoryDTO.getParentCategory().getId()));
                existingCategory.setParentCategory(parentCategory);
                
                // Validate circular reference
                categoryValidator.validateCircularReference(parentCategory, existingCategory);
            }
        } else {
            existingCategory.setParentCategory(null);
        }

        // Save updated category
        Category updatedCategory = categoryRepository.save(existingCategory);

        return categoryMapper.fromEntity(updatedCategory);
    }
}
