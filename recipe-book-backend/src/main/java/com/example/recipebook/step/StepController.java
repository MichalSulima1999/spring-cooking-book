package com.example.recipebook.step;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/step")
public record StepController(StepService stepService) {
    @Operation(summary = "Get a few steps by description")
    @GetMapping
    public ResponseEntity<List<Step>> getLimitedStepsByDescription(@Parameter(description = "Step description")
                                                                   @RequestParam(name = "description") String stepDescription) {
        return ResponseEntity.ok().body(stepService.getLimitedStepsByDescription(stepDescription));
    }
}
