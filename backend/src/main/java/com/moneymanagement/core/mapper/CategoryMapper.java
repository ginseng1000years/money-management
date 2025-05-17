package com.moneymanagement.core.mapper;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.model.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    public CategoryDTO fromEntity(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getImage(),
            category.getType(),
            fromEntity(category.getParentCategory()),
            category.getDescription()
        );
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setImage(dto.getImage());
        category.setType(dto.getType());
        if (dto.getParentCategory() != null) {
            category.setParentCategory(toEntity(dto.getParentCategory()));
        }
        category.setDescription(dto.getDescription());
        return category;
    }

    public void updateEntity(CategoryDTO dto, Category existingCategory) {
        if (dto == null || existingCategory == null) {
            return;
        }
        existingCategory.setName(dto.getName());
        existingCategory.setType(dto.getType());
        existingCategory.setImage(dto.getImage());
        existingCategory.setDescription(dto.getDescription());
        
        if (dto.getParentCategory() != null && dto.getParentCategory().getId() != null) {
            existingCategory.setParentCategory(toEntity(dto.getParentCategory()));
        } else {
            existingCategory.setParentCategory(null);
        }
    }
}