package com.studyplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * StudyPlan Entity for AI-Backed Study Planner
 * Represents user-created study plans with subjects and scheduling information
 * 
 * @author Garv Rahut & Shashwat Parashar
 */
@Entity
@Table(name = "study_plans")
public class StudyPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @NotBlank(message = "Study plan title is required")
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "At least one subject is required")
    @Column(nullable = false, columnDefinition = "JSON")
    private String subjects; // JSON array of subject names
    
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Positive(message = "Daily hours must be positive")
    @Column(name = "daily_hours", nullable = false, precision = 4, scale = 2)
    private BigDecimal dailyHours = new BigDecimal("2.0");
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel = DifficultyLevel.INTERMEDIATE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyPlanStatus status = StudyPlanStatus.DRAFT;
    
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    // Relationships
    @OneToMany(mappedBy = "studyPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StudySession> studySessions = new ArrayList<>();
    
    @OneToMany(mappedBy = "studyPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Recommendation> recommendations = new ArrayList<>();
    
    // Enums
    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
    
    public enum StudyPlanStatus {
        DRAFT, ACTIVE, COMPLETED, PAUSED
    }
    
    // Constructors
    public StudyPlan() {}
    
    public StudyPlan(String title, String subjects, LocalDate startDate, LocalDate endDate, BigDecimal dailyHours) {
        this.title = title;
        this.subjects = subjects;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyHours = dailyHours;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSubjects() {
        return subjects;
    }
    
    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public BigDecimal getDailyHours() {
        return dailyHours;
    }
    
    public void setDailyHours(BigDecimal dailyHours) {
        this.dailyHours = dailyHours;
    }
    
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public StudyPlanStatus getStatus() {
        return status;
    }
    
    public void setStatus(StudyPlanStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public List<StudySession> getStudySessions() {
        return studySessions;
    }
    
    public void setStudySessions(List<StudySession> studySessions) {
        this.studySessions = studySessions;
    }
    
    public List<Recommendation> getRecommendations() {
        return recommendations;
    }
    
    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
    
    // Utility methods
    public void addStudySession(StudySession studySession) {
        studySessions.add(studySession);
        studySession.setStudyPlan(this);
    }
    
    public void addRecommendation(Recommendation recommendation) {
        recommendations.add(recommendation);
        recommendation.setStudyPlan(this);
    }
    
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
    
    public boolean isActive() {
        return status == StudyPlanStatus.ACTIVE;
    }
    
    public boolean isCompleted() {
        return status == StudyPlanStatus.COMPLETED;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StudyPlan studyPlan = (StudyPlan) obj;
        return id != null && id.equals(studyPlan.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", dailyHours=" + dailyHours +
                ", status=" + status +
                '}';
    }
}