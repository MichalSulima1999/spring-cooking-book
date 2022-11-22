package com.example.recipebook.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public List<Category> getLimitedCategoriesByName(String name) {
        return categoryRepo.findTop5ByNameContainsIgnoreCase(name);
    }
}
