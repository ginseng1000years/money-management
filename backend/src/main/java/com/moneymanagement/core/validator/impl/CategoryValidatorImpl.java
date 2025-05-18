package com.moneymanagement.core.validator.impl;

import com.moneymanagement.core.validator.CategoryValidator;
import org.springframework.stereotype.Service;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.model.Category;

@Service
public class CategoryValidatorImpl implements CategoryValidator {

    /**
     * Validates the given CategoryDTO object.
     * Checks for null values and ensures that name, image, and type fields are valid.
     *
     * @param categoryDTO The CategoryDTO object to validate.
     * @throws IllegalArgumentException if any validation rule is violated.
     */
    @Override
public void validate(CategoryDTO categoryDTO) {
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
    
        /**
     * Validates that there are no circular references in the category hierarchy.
     * Traverses up the parent chain from the given parent category to ensure
     * the child category doesn't appear in its own ancestry.
     *
     * @param parent The potential parent category to check
     * @param child The child category being assigned
     * @throws IllegalArgumentException if a circular reference is detected
     */
    @Override
public void validateCircularReference(Category parent, Category child) {
        Category current = parent;
        while (current != null) {
            if (current.getId() != null && current.getId().equals(child.getId())) {
                throw new IllegalArgumentException("Circular reference detected in category hierarchy");
            }
            current = current.getParentCategory();
        }
    }
}
