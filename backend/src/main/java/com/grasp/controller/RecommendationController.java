package com.grasp.controller;

import com.grasp.dto.GenerateRecommendationsRequest;
import com.grasp.model.Recommendation;
import com.grasp.service.RecommendationEngineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationEngineService recommendationEngineService;

    public RecommendationController(RecommendationEngineService recommendationEngineService) {
        this.recommendationEngineService = recommendationEngineService;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Recommendation>> generate(@Valid @RequestBody GenerateRecommendationsRequest request) {
        List<Recommendation> recommendations = recommendationEngineService.generateRecommendations(
            request.getSessions(), request.getPlan()
        );
        return ResponseEntity.ok(recommendations);
    }
}




