package com.grasp.dto;

import com.grasp.model.StudyPlan;
import com.grasp.model.StudySession;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class GenerateRecommendationsRequest {
    @NotNull
    private StudyPlan plan;
    @NotNull
    private List<StudySession> sessions;

    public StudyPlan getPlan() { return plan; }
    public void setPlan(StudyPlan plan) { this.plan = plan; }

    public List<StudySession> getSessions() { return sessions; }
    public void setSessions(List<StudySession> sessions) { this.sessions = sessions; }
}




