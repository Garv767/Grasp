package com.studyplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Recommendation Entity for AI-Backed Study Planner
 * Represents AI-generated recommendations for users
 * 
 * @author Garv Rahut & Shashwat Parashar
 */
@Entity
@Table(name = "recommendations")
public class Recommendation {
    
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
    
    @NotBlank(message = "Recommendations JSON is required")
    @Column(name = "recommendations_json", nullable = false, columnDefinition = "JSON")
    private String recommendationsJson;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_type", nullable = false)
    private RecommendationType recommendationType;
    
    @DecimalMin(value = "0.0", message = "Confidence score must be between 0 and 1")
    @DecimalMax(value = "1.0", message = "Confidence score must be between 0 and 1")
    @Column(name = "confidence_score", precision = 3, scale = 2)
    private BigDecimal confidenceScore;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationStatus status = RecommendationStatus.PENDING;
    
    @Column(name = "applied_date")
    private LocalDateTime appliedDate;
    
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @Column(name = "expires_date")
    private LocalDateTime expiresDate;
    
    // Enums
    public enum RecommendationType {
        SCHEDULE_ADJUSTMENT,
        DIFFICULTY_CHANGE, 
        TIME_ALLOCATION,
        SUBJECT_PRIORITY
    }
    
    public enum RecommendationStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        EXPIRED
    }
    
    // Constructors
    public Recommendation() {}
    
    public Recommendation(String recommendationsJson, RecommendationType recommendationType, BigDecimal confidenceScore) {
        this.recommendationsJson = recommendationsJson;
        this.recommendationType = recommendationType;
        this.confidenceScore = confidenceScore;
        this.expiresDate = LocalDateTime.now().plusDays(7); // Default 7 days expiration
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
    
    public String getRecommendationsJson() {
        return recommendationsJson;
    }
    
    public void setRecommendationsJson(String recommendationsJson) {
        this.recommendationsJson = recommendationsJson;
    }
    
    public RecommendationType getRecommendationType() {
        return recommendationType;
    }
    
    public void setRecommendationType(RecommendationType recommendationType) {
        this.recommendationType = recommendationType;
    }
    
    public BigDecimal getConfidenceScore() {
        return confidenceScore;
    }
    
    public void setConfidenceScore(BigDecimal confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
    
    public RecommendationStatus getStatus() {
        return status;
    }
    
    public void setStatus(RecommendationStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }
    
    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getExpiresDate() {
        return expiresDate;
    }
    
    public void setExpiresDate(LocalDateTime expiresDate) {
        this.expiresDate = expiresDate;
    }
    
    // Utility methods
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
    
    public Long getStudyPlanId() {
        return studyPlan != null ? studyPlan.getId() : null;
    }
    
    public boolean isPending() {
        return status == RecommendationStatus.PENDING && 
               (expiresDate == null || expiresDate.isAfter(LocalDateTime.now()));
    }
    
    public boolean isExpired() {
        return expiresDate != null && expiresDate.isBefore(LocalDateTime.now());
    }
    
    public void accept() {
        this.status = RecommendationStatus.ACCEPTED;
        this.appliedDate = LocalDateTime.now();
    }
    
    public void reject() {
        this.status = RecommendationStatus.REJECTED;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recommendation that = (Recommendation) obj;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", recommendationType=" + recommendationType +
                ", confidenceScore=" + confidenceScore +
                ", status=" + status +
                ", createdDate=" + createdDate +
                '}';
    }
}