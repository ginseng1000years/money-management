package com.moneymanagement.core.controller;

import com.moneymanagement.core.dto.CategoryDTO;
import com.moneymanagement.core.model.Category;
import com.moneymanagement.core.repository.CategoryRepository;
import com.moneymanagement.core.service.CategoryService;
import com.moneymanagement.core.controller.CategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository);
        categoryController = new CategoryController();
        // Inject the real service into the controller
        try {
            java.lang.reflect.Field serviceField = CategoryController.class.getDeclaredField("categoryService");
            serviceField.setAccessible(true);
            serviceField.set(categoryController, categoryService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAllCategories() {
        List<CategoryDTO> mockCategories = new ArrayList<>();
        mockCategories.add(new CategoryDTO("1", "Food", "food.png", "expense", null, null));
        mockCategories.add(new CategoryDTO("2", "Salary", "salary.png", "income", null, null));

        when(categoryRepository.findAll()).thenReturn(new ArrayList<>()); // Mock repository call

        List<CategoryDTO> result = categoryController.getAllCategories();

        assertEquals(0, result.size()); // Since repository returns empty list

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testAddCategory() {
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
}
