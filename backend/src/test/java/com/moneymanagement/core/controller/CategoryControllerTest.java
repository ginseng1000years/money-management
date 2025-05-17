package com.moneymanagement.core.controller;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.model.Category;
import com.moneymanagement.core.repository.CategoryRepository;
import com.moneymanagement.core.service.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryController categoryController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        CategoryService categoryService = new CategoryService(categoryRepository);
        categoryController = new CategoryController(categoryService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Tests that updating a category with valid input successfully updates the existing category.
     * Verifies that:
     * - The repository's findById and save methods are called exactly once
     * - The returned CategoryDTO contains the updated values
     */
    @Test
    void updateCategory_shouldUpdateExistingCategory_whenValidInputProvided() {
        String categoryId = "4";
        CategoryDTO updateDTO = new CategoryDTO(null, "Updated Name", "updated.png", "income", null, null);
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Name");
        existingCategory.setImage("old.png");
        existingCategory.setType("expense");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updateDTO.getName());
        updatedCategory.setImage(updateDTO.getImage());
        updatedCategory.setType(updateDTO.getType());

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryDTO result = categoryController.updateCategory(categoryId, updateDTO);

        assertEquals(categoryId, result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals("updated.png", result.getImage());
        assertEquals("income", result.getType());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    /**
     * Tests that retrieving all categories returns paginated and sorted results.
     * Verifies:
     * - Correct pagination (page number, size, total elements)
     * - Proper sorting in both ascending and descending order
     * - Repository's findAll method is called with correct PageRequest
     */
    @Test
    void getAllCategories_shouldReturnPaginatedAndSortedCategories() {
        // Create test data
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(new Category("Bonus", "bonus.png", "income", null, null));
        mockCategories.add(new Category("Food", "food.png", "expense", null, null));
        mockCategories.add(new Category("Salary", "salary.png", "income", null, null));
        mockCategories.add(new Category("Transport", "transport.png", "expense", null, null));

        // Test pagination
        PageRequest pageRequest1 = PageRequest.of(0, 2, Sort.Direction.ASC, "name");
        when(categoryRepository.findAll(pageRequest1)).thenReturn(new org.springframework.data.domain.PageImpl<>(
            mockCategories.subList(0, 2), pageRequest1, mockCategories.size()));

        Page<CategoryDTO> result1 = categoryController.getAllCategories(0, 2, "name", "asc");
        
        assertEquals(0, result1.getNumber());
        assertEquals(2, result1.getSize());
        assertEquals(4, result1.getTotalElements());
        assertEquals(2, result1.getContent().size());
        assertEquals("Bonus", result1.getContent().get(0).getName());
        assertEquals("Food", result1.getContent().get(1).getName());

        // Test sorting descending
        PageRequest pageRequest2 = PageRequest.of(0, 2, Sort.Direction.DESC, "name");
        when(categoryRepository.findAll(pageRequest2)).thenReturn(new org.springframework.data.domain.PageImpl<>(
            mockCategories.subList(2, 4), pageRequest2, mockCategories.size()));

        Page<CategoryDTO> result2 = categoryController.getAllCategories(0, 2, "name", "desc");
        
        assertEquals(0, result2.getNumber());
        assertEquals(2, result2.getSize());
        assertEquals(4, result2.getTotalElements());
        assertEquals(2, result2.getContent().size());
        assertEquals("Salary", result2.getContent().get(0).getName());
        assertEquals("Transport", result2.getContent().get(1).getName());

        verify(categoryRepository, times(2)).findAll(any(PageRequest.class));
    }

    /**
     * Tests that adding a new category with valid input successfully creates the category.
     * Verifies:
     * - The repository's save method is called exactly once
     * - The returned CategoryDTO contains the correct values
     */
    @Test
    void addCategory_shouldCreateNewCategory_whenValidInputProvided() {
        CategoryDTO inputCategory = new CategoryDTO(null, "Travel", "travel.png", "expense", null, null);
        Category savedCategory = new Category();
        savedCategory.setId("3");
        savedCategory.setName("Travel");
        savedCategory.setImage("travel.png");
        savedCategory.setType("expense");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDTO result = categoryController.addCategory(inputCategory);

        assertEquals("3", result.getId());
        assertEquals("Travel", result.getName());
        assertEquals("travel.png", result.getImage());
        assertEquals("expense", result.getType());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    /**
     * Tests that deleting a category with an existing ID successfully removes it.
     * Verifies:
     * - The repository's deleteById method is called exactly once with the correct ID
     */
    @Test
    void deleteCategory_shouldRemoveCategory_whenIdExists() {
        String categoryId = "3";

        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryController.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
