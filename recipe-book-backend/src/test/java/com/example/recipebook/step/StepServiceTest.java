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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StepServiceTest {
    @Autowired
    private StepService stepService;

    @Autowired
    private StepRepo stepRepo;

    @BeforeAll
    private void setup() {
        List<Step> steps = new ArrayList<>(Arrays.asList(
                new Step("Chop onions"), new Step("Chop meat"),
                new Step("Chop carrots"), new Step("Glaze the onion"),
                new Step("Fry the meat"), new Step("Cook the carrot")));
        stepRepo.saveAll(steps);
    }

    @AfterAll
    private void clearDb() {
        stepRepo.deleteAll();
    }

    @Test
    void getLimitedStepsByDescription() {
        assertEquals(3, stepService.getLimitedStepsByDescription("Chop").size());
        assertEquals(1, stepService.getLimitedStepsByDescription("Glaze the onion").size());
        assertEquals(2, stepService.getLimitedStepsByDescription("carrot").size());
    }
}