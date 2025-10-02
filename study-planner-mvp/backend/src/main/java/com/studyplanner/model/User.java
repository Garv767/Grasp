package com.studyplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User Entity for AI-Backed Study Planner
 * Represents registered users with authentication and profile information
 * 
 * @author Garv Rahut & Shashwat Parashar
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @JsonIgnore // Never serialize password hash
    @NotBlank(message = "Password is required")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Column(name = "study_preferences", columnDefinition = "JSON")
    private String studyPreferences; // JSON string for flexibility
    
    @Column(length = 50)
    private String timezone = "UTC";
    
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;
    
    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Avoid circular serialization
    private List<StudyPlan> studyPlans = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StudySession> studySessions = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Recommendation> recommendations = new ArrayList<>();
    
    // Constructors
    public User() {}
    
    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }
    
    public User(String username, String email, String passwordHash, String firstName, String lastName) {
        this(username, email, passwordHash);
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getStudyPreferences() {
        return studyPreferences;
    }
    
    public void setStudyPreferences(String studyPreferences) {
        this.studyPreferences = studyPreferences;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
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
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public List<StudyPlan> getStudyPlans() {
        return studyPlans;
    }
    
    public void setStudyPlans(List<StudyPlan> studyPlans) {
        this.studyPlans = studyPlans;
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
    @JsonProperty("fullName")
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return username;
    }
    
    public void addStudyPlan(StudyPlan studyPlan) {
        studyPlans.add(studyPlan);
        studyPlan.setUser(this);
    }
    
    public void addStudySession(StudySession studySession) {
        studySessions.add(studySession);
        studySession.setUser(this);
    }
    
    public void addRecommendation(Recommendation recommendation) {
        recommendations.add(recommendation);
        recommendation.setUser(this);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id != null && id.equals(user.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", active=" + active +
                '}';
    }
}