package com.grasp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class StudyPlan {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private LocalDate deadline;
    private StudyPlanStatus status;
    private List<Subject> subjects;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum StudyPlanStatus {
        ACTIVE,
        COMPLETED,
        PAUSED
    }

    // Constructors
    public StudyPlan() {}

    public StudyPlan(Long userId, String title, String description, LocalDate deadline) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = StudyPlanStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public StudyPlanStatus getStatus() { return status; }
    public void setStatus(StudyPlanStatus status) { this.status = status; }

    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
