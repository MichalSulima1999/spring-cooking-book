package com.example.recipebook.diet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DietService {
    private final DietRepo dietRepo;

    public List<Diet> getAllDiets() {
        return dietRepo.findAll();
    }

    public List<Diet> getLimitedDietsByName(String name) {
        return dietRepo.findTop5ByNameContainsIgnoreCase(name);
    }
}
