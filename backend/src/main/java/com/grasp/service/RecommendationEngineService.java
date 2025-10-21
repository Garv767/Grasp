package com.grasp.service;

import com.grasp.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationEngineService {

    private static final double PERFORMANCE_THRESHOLD = 0.6;
    private static final double TIME_ADJUSTMENT_FACTOR = 1.2;
    private static final double CONFIDENCE_BASE = 0.7;

    /**
     * Generates AI recommendations based on study session data and plan information
     * 
     * @param sessions List of study sessions for analysis
     * @param plan The study plan containing subjects and time allocations
     * @return List of recommendations or empty list if no adjustments needed
     */
    public List<Recommendation> generateRecommendations(List<StudySession> sessions, StudyPlan plan) {
        List<Recommendation> recommendations = new ArrayList<>();
        
        if (sessions.isEmpty() || plan.getSubjects() == null) {
            return recommendations;
        }

        // Calculate performance metrics per subject
        Map<Long, SubjectPerformanceMetrics> subjectMetrics = calculateSubjectPerformanceMetrics(sessions, plan);
        
        // Generate recommendations for each subject
        for (Subject subject : plan.getSubjects()) {
            SubjectPerformanceMetrics metrics = subjectMetrics.get(subject.getId());
            
            if (metrics != null) {
                Recommendation recommendation = analyzeSubjectPerformance(subject, metrics);
                if (recommendation != null) {
                    recommendation.setPlanId(plan.getId());
                    recommendations.add(recommendation);
                }
            }
        }
        
        return recommendations;
    }

    /**
     * Analyzes individual subject performance and generates specific recommendations
     */
    private Recommendation analyzeSubjectPerformance(Subject subject, SubjectPerformanceMetrics metrics) {
        
        // Check for low performance requiring time adjustment
        if (metrics.getAveragePerformance() < PERFORMANCE_THRESHOLD) {
            int currentTime = subject.getTimeAllocationMinutes();
            int recommendedTime = (int) (currentTime * TIME_ADJUSTMENT_FACTOR);
            
            double confidence = calculateConfidence(metrics.getSessionCount(), metrics.getAveragePerformance());
            
            return new Recommendation(
                Recommendation.RecommendationType.TIME_ADJUSTMENT,
                subject.getId(),
                String.valueOf(recommendedTime),
                String.format("Performance below %.0f%% threshold (%.1f%%). Increase study time by %.0f%% to improve learning outcomes.",
                    PERFORMANCE_THRESHOLD * 100, metrics.getAveragePerformance() * 100, (TIME_ADJUSTMENT_FACTOR - 1) * 100)
            );
        }
        
        // Check for consistently high performance suggesting difficulty increase
        if (metrics.getAveragePerformance() > 0.85 && metrics.getSessionCount() >= 3) {
            String newDifficulty = suggestDifficultyIncrease(subject.getDifficultyLevel());
            
            if (newDifficulty != null) {
                double confidence = calculateConfidence(metrics.getSessionCount(), metrics.getAveragePerformance());
                
                return new Recommendation(
                    Recommendation.RecommendationType.DIFFICULTY_CHANGE,
                    subject.getId(),
                    newDifficulty,
                    String.format("Consistently high performance (%.1f%%) suggests ready for increased difficulty level.",
                        metrics.getAveragePerformance() * 100)
                );
            }
        }
        
        // Check for scheduling optimization opportunities
        if (metrics.getAverageSessionDuration() < 30 && metrics.getSessionCount() >= 2) {
            double confidence = calculateConfidence(metrics.getSessionCount(), metrics.getAveragePerformance());
            
            return new Recommendation(
                Recommendation.RecommendationType.SCHEDULE_MODIFICATION,
                subject.getId(),
                "EXTEND_SESSIONS",
                String.format("Short session durations (avg %.1f min) may impact learning effectiveness. Consider longer, focused sessions.",
                    metrics.getAverageSessionDuration())
            );
        }
        
        return null; // No recommendations needed
    }

    /**
     * Calculates comprehensive performance metrics for each subject
     */
    private Map<Long, SubjectPerformanceMetrics> calculateSubjectPerformanceMetrics(
            List<StudySession> sessions, StudyPlan plan) {
        
        Map<Long, SubjectPerformanceMetrics> metricsMap = new HashMap<>();
        
        // Group sessions by subject
        Map<Long, List<StudySession>> sessionsBySubject = sessions.stream()
            .collect(Collectors.groupingBy(StudySession::getSubjectId));
        
        // Calculate metrics for each subject
        for (Map.Entry<Long, List<StudySession>> entry : sessionsBySubject.entrySet()) {
            Long subjectId = entry.getKey();
            List<StudySession> subjectSessions = entry.getValue();
            
            if (!subjectSessions.isEmpty()) {
                SubjectPerformanceMetrics metrics = new SubjectPerformanceMetrics();
                
                // Basic metrics
                metrics.setSessionCount(subjectSessions.size());
                metrics.setAveragePerformance(
                    subjectSessions.stream()
                        .mapToDouble(s -> s.getPerformanceScore() / 10.0) // Normalize to 0-1
                        .average()
                        .orElse(0.0)
                );
                metrics.setAverageSessionDuration(
                    subjectSessions.stream()
                        .mapToInt(StudySession::getDurationMinutes)
                        .average()
                        .orElse(0.0)
                );
                metrics.setAverageCompletion(
                    subjectSessions.stream()
                        .mapToDouble(StudySession::getCompletionPercent)
                        .average()
                        .orElse(0.0)
                );
                
                // Trend analysis
                metrics.setPerformanceTrend(calculatePerformanceTrend(subjectSessions));
                metrics.setConsistencyScore(calculateConsistencyScore(subjectSessions));
                
                metricsMap.put(subjectId, metrics);
            }
        }
        
        return metricsMap;
    }

    /**
     * Calculates performance trend over time (positive/negative/stable)
     */
    private double calculatePerformanceTrend(List<StudySession> sessions) {
        if (sessions.size() < 2) return 0.0;
        
        // Sort by creation time
        List<StudySession> sortedSessions = sessions.stream()
            .sorted(Comparator.comparing(StudySession::getCreatedAt))
            .collect(Collectors.toList());
        
        double firstHalf = sortedSessions.subList(0, sortedSessions.size() / 2)
            .stream()
            .mapToDouble(s -> s.getPerformanceScore() / 10.0)
            .average()
            .orElse(0.0);
        
        double secondHalf = sortedSessions.subList(sortedSessions.size() / 2, sortedSessions.size())
            .stream()
            .mapToDouble(s -> s.getPerformanceScore() / 10.0)
            .average()
            .orElse(0.0);
        
        return secondHalf - firstHalf; // Positive = improving, Negative = declining
    }

    /**
     * Calculates consistency score based on performance variance
     */
    private double calculateConsistencyScore(List<StudySession> sessions) {
        if (sessions.size() < 2) return 1.0;
        
        double mean = sessions.stream()
            .mapToDouble(s -> s.getPerformanceScore() / 10.0)
            .average()
            .orElse(0.0);
        
        double variance = sessions.stream()
            .mapToDouble(s -> Math.pow(s.getPerformanceScore() / 10.0 - mean, 2))
            .average()
            .orElse(0.0);
        
        // Convert variance to consistency score (lower variance = higher consistency)
        return Math.max(0.0, 1.0 - Math.sqrt(variance));
    }

    /**
     * Suggests next difficulty level based on current performance
     */
    private String suggestDifficultyIncrease(Subject.DifficultyLevel currentLevel) {
        switch (currentLevel) {
            case BEGINNER:
                return "INTERMEDIATE";
            case INTERMEDIATE:
                return "ADVANCED";
            case ADVANCED:
                return null; // Already at highest level
            default:
                return null;
        }
    }

    /**
     * Calculates confidence score for recommendations
     */
    private double calculateConfidence(int sessionCount, double averagePerformance) {
        // Base confidence increases with more data points
        double sessionConfidence = Math.min(1.0, sessionCount / 5.0);
        
        // Performance confidence based on how far from threshold
        double performanceConfidence = Math.abs(averagePerformance - PERFORMANCE_THRESHOLD) * 2;
        
        return Math.min(1.0, CONFIDENCE_BASE + (sessionConfidence * 0.2) + (performanceConfidence * 0.1));
    }

    /**
     * Inner class to hold performance metrics for a subject
     */
    private static class SubjectPerformanceMetrics {
        private int sessionCount;
        private double averagePerformance;
        private double averageSessionDuration;
        private double averageCompletion;
        private double performanceTrend;
        private double consistencyScore;

        // Getters and Setters
        public int getSessionCount() { return sessionCount; }
        public void setSessionCount(int sessionCount) { this.sessionCount = sessionCount; }

        public double getAveragePerformance() { return averagePerformance; }
        public void setAveragePerformance(double averagePerformance) { this.averagePerformance = averagePerformance; }

        public double getAverageSessionDuration() { return averageSessionDuration; }
        public void setAverageSessionDuration(double averageSessionDuration) { this.averageSessionDuration = averageSessionDuration; }

        public double getAverageCompletion() { return averageCompletion; }
        public void setAverageCompletion(double averageCompletion) { this.averageCompletion = averageCompletion; }

        public double getPerformanceTrend() { return performanceTrend; }
        public void setPerformanceTrend(double performanceTrend) { this.performanceTrend = performanceTrend; }

        public double getConsistencyScore() { return consistencyScore; }
        public void setConsistencyScore(double consistencyScore) { this.consistencyScore = consistencyScore; }
    }
}
