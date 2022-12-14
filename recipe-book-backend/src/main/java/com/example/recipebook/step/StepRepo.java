package com.example.recipebook.step;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StepRepo extends JpaRepository<Step, Long> {
    Optional<Step> findByDescription(String description);

    List<Step> findTop3ByDescriptionContainsIgnoreCase(String description);
}
