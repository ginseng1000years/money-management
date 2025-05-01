package com.moneymanagement.core.config;

import com.moneymanagement.core.model.Category;
import com.moneymanagement.core.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                Category food = new Category("Food", "food.png", "expense", null, "Food related expenses");
                Category transport = new Category("Transport", "transport.png", "expense", null, "Transport related expenses");
                Category entertainment = new Category("Entertainment", "entertainment.png", "expense", null, "Entertainment related expenses");
                Category utilities = new Category("Utilities", "utilities.png", "expense", null, "Utility bills and expenses");
                Category health = new Category("Health", "health.png", "expense", null, "Health and medical expenses");

                categoryRepository.save(food);
                categoryRepository.save(transport);
                categoryRepository.save(entertainment);
                categoryRepository.save(utilities);
                categoryRepository.save(health);
            }
        };
    }
}
