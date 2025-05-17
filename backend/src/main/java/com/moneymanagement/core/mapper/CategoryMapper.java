package com.moneymanagement.core.mapper;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO fromEntity(Category category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryDTO dto);

    @Mapping(target = "parentCategory", source = "parentCategory")
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Category existingCategory, @NonNull CategoryDTO dto);
}