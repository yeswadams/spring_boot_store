package com.masoko.store.features.product;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

import java.util.Arrays;

@Component
public class CategoryDataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryDataSeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> defaultCategories = Arrays.asList(
                "Beauty",
                "Electronic",
                "Women's Fashion",
                "Men's Fashion",
                "Girls' Fashion",
                "Boys' Fashion",
                "Health & House Hold",
                "Home & Kitchen",
                "Pet Supplies",
                "Sports"
        );

        for(String name : defaultCategories) {
            if(!categoryRepository.existsByName(name)) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
            }
        }
    }
}
