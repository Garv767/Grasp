# AI-Backed Study Planner MVP Design Document

**Project Leaders:** Garv Rahut & Shashwat Parashar  
**Document Version:** 1.0  
**Date:** October 2025

## Executive Summary

The AI-Backed Study Planner MVP is a cross-platform application that provides personalized, adaptive study scheduling based on user progress and performance analytics. The system combines a Java Spring Boot backend with web and mobile frontends to deliver intelligent study recommendations.

---

## 1. MVP Feature List (Core Features)

### 1.1 Study Plan Creation & Management
- **User Story:** As a student, I want to create customized study plans for my subjects/topics
- **Features:**
  - Create study plans with subjects, topics, and time allocations
  - Set study goals and deadlines
  - View and edit existing study plans
- **Priority:** High

### 1.2 Progress Tracking & Analytics
- **User Story:** As a student, I want to track my study sessions and see my progress
- **Features:**
  - Log study sessions with duration and topic
  - Track completion rates and performance scores
  - Visual progress dashboards with charts
- **Priority:** High

### 1.3 AI-Powered Schedule Adaptation
- **User Story:** As a student, I want my study schedule to adapt based on my performance
- **Features:**
  - Automatic schedule adjustments based on progress data
  - Difficulty-based time allocation recommendations
  - Performance-based topic prioritization
- **Priority:** High

### 1.4 User Authentication & Profile Management
- **User Story:** As a user, I want to securely access my personalized study data
- **Features:**
  - User registration and login
  - Profile management with study preferences
  - Secure data storage
- **Priority:** Medium

### 1.5 Notification System
- **User Story:** As a student, I want reminders for my study sessions
- **Features:**
  - Study session reminders
  - Goal deadline notifications
  - Progress milestone alerts
- **Priority:** Medium

---

## 2. Technical Specification

### 2.1 Tech Stack Overview

#### Frontend Technologies
- **Web Frontend:** JSP (JavaServer Pages) with Thymeleaf templating
- **Mobile Frontend:** Android (Java/Kotlin)
- **UI Framework:** Bootstrap 5 for responsive web design
- **Charts/Visualization:** Chart.js for progress analytics

#### Backend Technologies
- **Application Framework:** Java Spring Boot 3.x
- **Security:** Spring Security for authentication
- **Data Access:** Spring Data JPA with Hibernate
- **API Documentation:** Swagger/OpenAPI 3
- **Build Tool:** Maven

#### Database Technologies
- **Primary Database:** MySQL 8.0+
- **Alternative/Development:** SQLite (for local development)
- **Cloud Option:** Firebase (for mobile-first deployments)

#### AI/ML Technologies
- **ML Framework:** Python scikit-learn (via REST API)
- **Model Type:** Decision Tree/Random Forest for recommendation
- **Integration:** RESTful ML service called from Spring Boot
- **Data Processing:** Pandas for data preparation

### 2.2 Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐
│   Web Frontend  │    │ Mobile Frontend │
│  (JSP/Thymeleaf)│    │    (Android)    │
└─────────────────┘    └─────────────────┘
         │                       │
         └───────────┬───────────┘
                     │
         ┌─────────────────────────┐
         │   Spring Boot Backend   │
         │   (REST API + Security) │
         └─────────────────────────┘
                     │
         ┌─────────────────────────┐
         │      MySQL Database     │
         │  (Users, Plans, Progress)│
         └─────────────────────────┘
                     │
         ┌─────────────────────────┐
         │   ML Recommendation     │
         │   Engine (Python API)   │
         └─────────────────────────┘
```

---

## 3. Core API Endpoints

### 3.1 User Management API
```http
POST /api/v1/users/register
POST /api/v1/users/login
GET  /api/v1/users/profile
PUT  /api/v1/users/profile
```

**Primary Endpoint:** `POST /api/v1/users/register`
- **Purpose:** User account creation and authentication
- **Request:** `{ "username": "string", "email": "string", "password": "string" }`
- **Response:** `{ "userId": "long", "token": "string", "message": "string" }`

### 3.2 Study Plan Management API
```http
POST /api/v1/study-plans
GET  /api/v1/study-plans
GET  /api/v1/study-plans/{id}
PUT  /api/v1/study-plans/{id}
DELETE /api/v1/study-plans/{id}
```

**Primary Endpoint:** `POST /api/v1/study-plans`
- **Purpose:** Create and manage personalized study plans
- **Request:** 
```json
{
  "title": "string",
  "subjects": ["string"],
  "startDate": "date",
  "endDate": "date",
  "dailyHours": "number"
}
```
- **Response:** `{ "planId": "long", "schedule": "object", "message": "string" }`

### 3.3 Progress Tracking API
```http
POST /api/v1/progress/sessions
GET  /api/v1/progress/analytics/{userId}
GET  /api/v1/progress/recommendations/{userId}
```

**Primary Endpoint:** `POST /api/v1/progress/sessions`
- **Purpose:** Log study sessions and trigger AI recommendations
- **Request:** 
```json
{
  "userId": "long",
  "subject": "string",
  "duration": "number",
  "performanceScore": "number",
  "completionRate": "number"
}
```
- **Response:** `{ "sessionId": "long", "updatedRecommendations": "object" }`

---

## 4. AI Functionality (Recommendation Engine)

### 4.1 Core AI Task
**Primary Function:** Adaptive Study Schedule Optimization

The ML model performs one specific, measurable task for the MVP:
- **Input:** User's historical study sessions (duration, subject, performance scores, completion rates)
- **Output:** Optimized daily study schedule with subject priorities and time allocations

### 4.2 Simple Algorithm Approach
For MVP, the recommendation engine uses a rule-based system with basic ML:

1. **Performance Analysis:** 
   - Track completion rates per subject
   - Calculate average performance scores
   - Identify struggling areas (low completion/performance)

2. **Schedule Adaptation Logic:**
   - Allocate more time to subjects with completion rate < 70%
   - Prioritize difficult topics during user's most productive hours
   - Reduce time for mastered subjects (completion rate > 90%)

3. **ML Model Type:** Decision Tree Classifier
   - **Features:** [subject_difficulty, past_performance, time_spent, completion_rate]
   - **Target:** [time_allocation_category] (low/medium/high priority)
   - **Training Data:** Simulated user study patterns + real user feedback

### 4.3 Recommendation Output Format
```json
{
  "recommendations": {
    "dailySchedule": [
      {
        "subject": "Mathematics",
        "priority": "high",
        "recommendedTime": 90,
        "reason": "Low completion rate (65%)"
      }
    ],
    "adaptationReason": "Increased focus on struggling subjects",
    "confidenceScore": 0.85
  }
}
```

---

## 5. Implementation Steps

### Step 1: Backend Foundation (Week 1-2)
#### Database Schema Design
- **Users Table:** id, username, email, password_hash, created_date
- **Study_Plans Table:** id, user_id, title, subjects, start_date, end_date, daily_hours
- **Study_Sessions Table:** id, user_id, subject, duration, performance_score, completion_rate, session_date
- **Recommendations Table:** id, user_id, recommendations_json, created_date

#### Spring Boot Setup
- Initialize Spring Boot project with dependencies
- Configure MySQL connection and JPA entities
- Set up Spring Security for authentication
- Create repository interfaces for data access

### Step 2: Frontend Integration (Week 3-4)
#### Web Frontend (JSP/Thymeleaf)
- Create user registration/login pages
- Build study plan creation and management interface
- Develop progress tracking dashboard
- Implement responsive design with Bootstrap

#### Mobile Frontend Setup
- Initialize Android project structure
- Create basic UI screens (login, dashboard, progress)
- Implement REST API integration with Retrofit
- Set up local data storage for offline capability

### Step 3: AI Loop Implementation (Week 5-6)
#### ML Model Development
- Create Python ML service with FastAPI
- Implement decision tree model for recommendations
- Create training data pipeline with mock user data
- Set up model inference endpoint

#### Integration & Testing
- Connect Spring Boot backend to ML service
- Implement recommendation triggering logic
- Test end-to-end AI adaptation workflow
- Deploy MVP for user testing

---

## 6. Success Metrics for MVP

### 6.1 Technical Metrics
- API response time < 500ms for core endpoints
- 99% uptime for backend services
- Mobile app crash rate < 1%
- Database query performance < 100ms average

### 6.2 User Experience Metrics
- User registration completion rate > 80%
- Daily active usage > 15 minutes
- Study plan completion rate improvement > 20%
- User satisfaction score > 4.0/5.0

### 6.3 AI Performance Metrics
- Recommendation accuracy > 70% (based on user feedback)
- Schedule adaptation response time < 2 seconds
- Model confidence score > 0.75 for recommendations

---

## 7. Development Timeline

| Phase | Duration | Key Deliverables |
|-------|----------|------------------|
| **Setup & Architecture** | Week 1 | Project structure, database schema, API design |
| **Backend Core** | Week 2 | Spring Boot APIs, authentication, data models |
| **Frontend Web** | Week 3 | JSP/Thymeleaf UI, user workflows |
| **Frontend Mobile** | Week 4 | Android app basic functionality |
| **AI Integration** | Week 5 | ML model, recommendation engine |
| **Testing & Deployment** | Week 6 | End-to-end testing, MVP deployment |

---

## 8. Risk Mitigation

### 8.1 Technical Risks
- **Database Performance:** Implement connection pooling and query optimization
- **AI Model Accuracy:** Start with rule-based fallback, iterate with user feedback
- **Cross-Platform Sync:** Implement robust API error handling and retry logic

### 8.2 User Adoption Risks
- **Complex UI:** Focus on intuitive design with user testing
- **Value Proposition:** Clearly demonstrate AI recommendations benefits
- **Data Privacy:** Implement transparent privacy policy and secure data handling

---

*This document serves as the foundation for the AI-Backed Study Planner MVP development by Garv Rahut and Shashwat Parashar.*