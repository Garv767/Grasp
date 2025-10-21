package com.grasp.model;

import java.time.LocalDateTime;

public class Subject {
    private Long id;
    private Long planId;
    private String name;
    private DifficultyLevel difficultyLevel;
    private Integer timeAllocationMinutes;
    private Double currentProgressPercent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    // Constructors
    public Subject() {}

    public Subject(Long planId, String name, DifficultyLevel difficultyLevel, Integer timeAllocationMinutes) {
        this.planId = planId;
        this.name = name;
        this.difficultyLevel = difficultyLevel;
        this.timeAllocationMinutes = timeAllocationMinutes;
        this.currentProgressPercent = 0.0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public Integer getTimeAllocationMinutes() { return timeAllocationMinutes; }
    public void setTimeAllocationMinutes(Integer timeAllocationMinutes) { this.timeAllocationMinutes = timeAllocationMinutes; }

    public Double getCurrentProgressPercent() { return currentProgressPercent; }
    public void setCurrentProgressPercent(Double currentProgressPercent) { this.currentProgressPercent = currentProgressPercent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
