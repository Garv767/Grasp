package com.studyplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * StudySession Entity for AI-Backed Study Planner
 * Represents individual study sessions logged by users
 * 
 * @author Garv Rahut & Shashwat Parashar
 */
@Entity
@Table(name = "study_sessions")
public class StudySession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_plan_id")
    @JsonIgnore
    private StudyPlan studyPlan;
    
    @NotBlank(message = "Subject is required")
    @Column(nullable = false, length = 100)
    private String subject;
    
    @Column(length = 200)
    private String topic;
    
    @Positive(message = "Duration must be positive")
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
    
    @DecimalMin(value = "0.0", message = "Performance score must be between 0 and 1")
    @DecimalMax(value = "1.0", message = "Performance score must be between 0 and 1")
    @Column(name = "performance_score", precision = 3, scale = 2)
    private BigDecimal performanceScore;
    
    @DecimalMin(value = "0.0", message = "Completion rate must be between 0 and 1")
    @DecimalMax(value = "1.0", message = "Completion rate must be between 0 and 1")
    @Column(name = "completion_rate", precision = 3, scale = 2)
    private BigDecimal completionRate;
    
    @Min(value = 1, message = "Difficulty rating must be between 1 and 5")
    @Max(value = 5, message = "Difficulty rating must be between 1 and 5")
    @Column(name = "difficulty_rating")
    private Integer difficultyRating;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "session_date", updatable = false)
    private LocalDateTime sessionDate;
    
    @Min(value = 1, message = "Mood rating must be between 1 and 5")
    @Max(value = 5, message = "Mood rating must be between 1 and 5")
    @Column(name = "mood_rating")
    private Integer moodRating;
    
    // Constructors
    public StudySession() {}
    
    public StudySession(String subject, Integer durationMinutes, BigDecimal performanceScore, BigDecimal completionRate) {
        this.subject = subject;
        this.durationMinutes = durationMinutes;
        this.performanceScore = performanceScore;
        this.completionRate = completionRate;
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
    
    public StudyPlan getStudyPlan() {
        return studyPlan;
    }
    
    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public BigDecimal getPerformanceScore() {
        return performanceScore;
    }
    
    public void setPerformanceScore(BigDecimal performanceScore) {
        this.performanceScore = performanceScore;
    }
    
    public BigDecimal getCompletionRate() {
        return completionRate;
    }
    
    public void setCompletionRate(BigDecimal completionRate) {
        this.completionRate = completionRate;
    }
    
    public Integer getDifficultyRating() {
        return difficultyRating;
    }
    
    public void setDifficultyRating(Integer difficultyRating) {
        this.difficultyRating = difficultyRating;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getSessionDate() {
        return sessionDate;
    }
    
    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }
    
    public Integer getMoodRating() {
        return moodRating;
    }
    
    public void setMoodRating(Integer moodRating) {
        this.moodRating = moodRating;
    }
    
    // Utility methods
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
    
    public Long getStudyPlanId() {
        return studyPlan != null ? studyPlan.getId() : null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StudySession that = (StudySession) obj;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "StudySession{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", topic='" + topic + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", performanceScore=" + performanceScore +
                ", sessionDate=" + sessionDate +
                '}';
    }
}