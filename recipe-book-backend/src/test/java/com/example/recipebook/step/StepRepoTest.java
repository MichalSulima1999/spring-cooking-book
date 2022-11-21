package com.example.recipebook.step;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StepRepoTest {
    @Autowired
    private StepRepo stepRepo;

    private Step testStep;

    @BeforeAll
    private void setup() {
        List<Step> steps = new ArrayList<>(Arrays.asList(
                new Step("Chop onions"), new Step("Chop meat"),
                new Step("Chop carrots"), new Step("Glaze the onion"),
                new Step("Fry the meat"), new Step("Cook the carrot")));
        stepRepo.saveAll(steps);

        testStep = stepRepo.save(new Step("Mince pork"));
    }

    @AfterAll
    private void clearDb() {
        stepRepo.deleteAll();
    }

    @Test
    void findByDescription() {
        assertTrue(stepRepo.findByDescription("Mince pork").isPresent(),
                "Step should be found");
        assertEquals(testStep.getId(), stepRepo.findByDescription("Mince pork").get().getId(),
                "Step ids should be equal");
        assertFalse(stepRepo.findByDescription("Boil water").isPresent(),
                "Step should not be found");
    }

    @Test
    void findTop3ByDescriptionContainsIgnoreCase() {
        assertEquals(3, stepRepo
                .findTop3ByDescriptionContainsIgnoreCase("Chop").size(), "Should find 3 steps");
        assertEquals(1, stepRepo
                .findTop3ByDescriptionContainsIgnoreCase("Glaze the onion").size(), "Should find 1 step");
        assertEquals(2, stepRepo
                .findTop3ByDescriptionContainsIgnoreCase("carrot").size(), "Should find 2 steps");
    }
}