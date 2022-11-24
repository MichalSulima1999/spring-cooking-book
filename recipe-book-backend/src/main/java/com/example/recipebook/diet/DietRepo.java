package com.example.recipebook.diet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DietRepo extends JpaRepository<Diet, Long> {
    Optional<Diet> findByName(String name);

    List<Diet> findTop5ByNameContainsIgnoreCase(String name);


}
