package com.moneymanagement.core.validator;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.model.Category;

/**
 * Service class for validating CategoryDTO objects.
 * Provides methods to validate category fields and throws exceptions if validation fails.
 */
public interface CategoryValidator {
    void validate(CategoryDTO categoryDTO);
    void validateCircularReference(Category parent, Category child);
}