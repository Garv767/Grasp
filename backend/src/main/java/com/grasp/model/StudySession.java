package com.grasp.model;

import java.time.LocalDateTime;

public class StudySession {
    private Long id;
    private Long planId;
    private Long subjectId;
    private Integer durationMinutes;
    private Double completionPercent;
    private Integer performanceScore;
    private String notes;
    private LocalDateTime createdAt;

    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    // Constructors
    public StudySession() {}

    public StudySession(Long planId, Long subjectId, Integer durationMinutes, 
                       Double completionPercent, Integer performanceScore, String notes) {
        this.planId = planId;
        this.subjectId = subjectId;
        this.durationMinutes = durationMinutes;
        this.completionPercent = completionPercent;
        this.performanceScore = performanceScore;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Double getCompletionPercent() { return completionPercent; }
    public void setCompletionPercent(Double completionPercent) { this.completionPercent = completionPercent; }

    public Integer getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(Integer performanceScore) { this.performanceScore = performanceScore; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
