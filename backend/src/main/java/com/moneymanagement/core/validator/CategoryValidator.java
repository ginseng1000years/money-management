package com.moneymanagement.core.validator;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.model.Category;

/**
 * Utility class for validating CategoryDTO objects.
 * Provides a method to validate the fields of a CategoryDTO and throws exceptions if validation fails.
 */
public class CategoryValidator {

    /**
     * Validates the given CategoryDTO object.
     * Checks for null values and ensures that name, image, and type fields are valid.
     *
     * @param categoryDTO The CategoryDTO object to validate.
     * @throws IllegalArgumentException if any validation rule is violated.
     */
    public static void validate(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
        if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be blank");
        }
        if (categoryDTO.getImage() == null || categoryDTO.getImage().trim().isEmpty()) {
            throw new IllegalArgumentException("Category image must not be blank");
        }
        if (categoryDTO.getType() == null || 
            !(categoryDTO.getType().equalsIgnoreCase("income") || categoryDTO.getType().equalsIgnoreCase("expense"))) {
            throw new IllegalArgumentException("Category type must be either 'income' or 'expense'");
        }
    }
    
    public static void validateCircularReference(Category parent, Category child) {
        Category current = parent;
        while (current != null) {
            if (current.getId() != null && current.getId().equals(child.getId())) {
                throw new IllegalArgumentException("Circular reference detected in category hierarchy");
            }
            current = current.getParentCategory();
        }
    }
}
