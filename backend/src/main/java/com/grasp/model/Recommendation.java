package com.grasp.model;

import java.time.LocalDateTime;

public class Recommendation {
    private Long id;
    private Long planId;
    private Long subjectId;
    private RecommendationType type;
    private String value;
    private Double confidenceScore;
    private String reasoning;
    private RecommendationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum RecommendationType {
        TIME_ADJUSTMENT,
        DIFFICULTY_CHANGE,
        SCHEDULE_MODIFICATION
    }

    public enum RecommendationStatus {
        PENDING,
        APPLIED,
        DISMISSED
    }

    // Constructors
    public Recommendation() {}

    public Recommendation(RecommendationType type, Long subjectId, String value, String reasoning) {
        this.type = type;
        this.subjectId = subjectId;
        this.value = value;
        this.reasoning = reasoning;
        this.status = RecommendationStatus.PENDING;
        this.confidenceScore = 0.0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public RecommendationType getType() { return type; }
    public void setType(RecommendationType type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }

    public RecommendationStatus getStatus() { return status; }
    public void setStatus(RecommendationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
