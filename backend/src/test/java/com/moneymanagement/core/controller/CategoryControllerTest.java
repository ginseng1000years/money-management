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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
        // Given
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

        // When
        CategoryDTO result = categoryController.updateCategory(categoryId, updateDTO);

        // Then
        assertAll("Verify all fields updated correctly",
            () -> assertEquals(categoryId, result.getId(), "ID should match"),
            () -> assertEquals("Updated Name", result.getName(), "Name should be updated"),
            () -> assertEquals("updated.png", result.getImage(), "Image should be updated"),
            () -> assertEquals("income", result.getType(), "Type should be updated")
        );

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    
    @Test
    void updateCategory_shouldSetParentCategory_whenParentIdProvided() {
        // Given
        String categoryId = "5";
        String parentId = "10";
        CategoryDTO updateDTO = new CategoryDTO(null, "Updated Name", "updated.png", "income", 
            new CategoryDTO(parentId, "Parent", "parent.png", "income", null, null), null);
            
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Name");
        
        Category parentCategory = new Category();
        parentCategory.setId(parentId);
        parentCategory.setName("Parent");
        
        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updateDTO.getName());
        updatedCategory.setParentCategory(parentCategory);

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.findById(parentId)).thenReturn(java.util.Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // When
        CategoryDTO result = categoryController.updateCategory(categoryId, updateDTO);

        // Then
        assertAll("Verify parent category is set",
            () -> assertEquals(parentId, result.getParentCategory().getId(), "Parent ID should match"),
            () -> assertEquals("Parent", result.getParentCategory().getName(), "Parent name should match")
        );
        
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).findById(parentId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    
    @Test
    void updateCategory_shouldClearParentCategory_whenParentIdNotProvided() {
        // Given
        String categoryId = "6";
        CategoryDTO updateDTO = new CategoryDTO(null, "Updated Name", "updated.png", "income", null, null);
        
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Name");
        existingCategory.setParentCategory(new Category("Old Parent", "parent.png", "income", null, null));
        
        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updateDTO.getName());
        updatedCategory.setParentCategory(null);

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // When
        CategoryDTO result = categoryController.updateCategory(categoryId, updateDTO);

        // Then
        assertNull(result.getParentCategory(), "Parent category should be null");
        
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    
    @Test
    void updateCategory_shouldNotLookupParent_whenParentCategoryHasNullId() {
        // Given
        String categoryId = "7";
        CategoryDTO updateDTO = new CategoryDTO(null, "Updated Name", "updated.png", "income", 
            new CategoryDTO(null, "Parent", "parent.png", "income", null, null), null);
            
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Name");
        
        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updateDTO.getName());

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // When
        CategoryDTO result = categoryController.updateCategory(categoryId, updateDTO);

        // Then
        assertEquals("Updated Name", result.getName());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
        
        // Verify parent category is set to null
        assertNull(result.getParentCategory(), "Parent category should be null when parent ID is null");
    }
    
    @Test
    void updateCategory_shouldThrowException_whenCategoryNotFound() {
        // Given
        String nonExistentId = "999";
        CategoryDTO updateDTO = new CategoryDTO(null, "Updated Name", "updated.png", "income", null, null);
        
        when(categoryRepository.findById(nonExistentId)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            categoryController.updateCategory(nonExistentId, updateDTO);
        }, "Should throw exception when category not found");
        
        verify(categoryRepository, times(1)).findById(nonExistentId);
        verify(categoryRepository, never()).save(any(Category.class));
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
        // Given
        CategoryDTO inputCategory = new CategoryDTO(null, "Travel", "travel.png", "expense", null, null);
        
        Category savedCategory = new Category();
        savedCategory.setId("3");
        savedCategory.setName("Travel");
        savedCategory.setImage("travel.png");
        savedCategory.setType("expense");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // When
        CategoryDTO result = categoryController.addCategory(inputCategory);

        // Then
        assertAll("Verify all fields are correctly set",
            () -> assertEquals("3", result.getId(), "ID should match"),
            () -> assertEquals("Travel", result.getName(), "Name should match"),
            () -> assertEquals("travel.png", result.getImage(), "Image should match"),
            () -> assertEquals("expense", result.getType(), "Type should match")
        );

        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    
    @Test
    void addCategory_shouldThrowException_whenInvalidInputProvided() {
        // Given
        CategoryDTO invalidCategory = new CategoryDTO(null, "", "", "", null, null);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            categoryController.addCategory(invalidCategory);
        }, "Should throw exception when invalid input provided");
        
        verify(categoryRepository, never()).save(any(Category.class));
    }

    /**
     * Tests that deleting a category with an existing ID successfully removes it.
     * Verifies:
     * - The repository's deleteById method is called exactly once with the correct ID
     */
    @Test
    void deleteCategory_shouldRemoveCategory_whenIdExists() {
        // Given
        String categoryId = "3";
        
        doNothing().when(categoryRepository).deleteById(categoryId);

        // When
        categoryController.deleteCategory(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
    
    @Test
    void deleteCategory_shouldNotThrowException_whenIdDoesNotExist() {
        // Given
        String nonExistentId = "999";
        
        doNothing().when(categoryRepository).deleteById(nonExistentId);

        // When & Then
        assertDoesNotThrow(() -> {
            categoryController.deleteCategory(nonExistentId);
        }, "Should not throw exception when deleting non-existent category");
        
        verify(categoryRepository, times(1)).deleteById(nonExistentId);
    }
}
