package com.example.recipebook.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public List<Category> getLimitedCategoriesByName(String name) {
        return categoryRepo.findTop5ByNameContainsIgnoreCase(name);
    }

    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }
}
