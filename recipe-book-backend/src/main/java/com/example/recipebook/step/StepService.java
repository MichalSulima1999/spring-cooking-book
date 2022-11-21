package com.example.recipebook.step;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StepService {
    private final StepRepo stepRepo;

    public List<Step> getLimitedStepsByDescription(String description) {
        return stepRepo.findTop3ByDescriptionContainsIgnoreCase(description);
    }
}
