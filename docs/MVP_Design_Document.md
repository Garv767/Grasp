# AI-Backed Study Planner and Progress Tracker - MVP Design Document

## Project Overview
**Project Name:** Grasp - AI-Backed Study Planner and Progress Tracker  
**Root Directory:** Grasp  
**Objective:** Rapidly build and validate a functional, adaptive study management tool with ML-powered recommendations

---

## 1. MVP Feature Roadmap

### Core Features (4 Essential Components)

1. **User Authentication**
   - User registration and login
   - Session management
   - Basic user profile management

2. **Study Goal Definition**
   - Create study plans with subjects and time allocations
   - Set difficulty levels and deadlines
   - Define learning objectives

3. **Session Progress Logging**
   - Log study sessions with duration and completion status
   - Track progress against goals
   - Record performance metrics (focus, comprehension, etc.)

4. **Basic AI Schedule Adjustment**
   - Simple Recommendation Engine analyzes progress patterns
   - Automatically adjusts remaining time allocations
   - Provides adaptive scheduling suggestions

---

## 2. Tech Stack Definition

### Frontend
- **Framework:** React.js (SPA)
- **State Management:** React Context API / useState
- **HTTP Client:** Axios
- **UI Framework:** Material-UI or Tailwind CSS
- **Build Tool:** Create React App

### Backend
- **Framework:** Java Spring Boot
- **Database Connectivity:** JDBC
- **Security:** Spring Security
- **API Documentation:** SpringDoc OpenAPI
- **Build Tool:** Maven

### Database
- **Primary Database:** PostgreSQL
- **Connection:** JDBC Driver
- **ORM:** Spring Data JDBC (minimal)

### AI/ML Component
- **Implementation:** Java-based simple algorithm
- **Location:** Spring Boot service layer
- **Processing:** In-memory calculations

---

## 3. Critical API Endpoints

### Essential REST Endpoints

1. **POST /api/auth/login**
   - **Resource:** Authentication
   - **Method:** POST
   - **Function:** User login and JWT token generation
   - **Request Body:** `{ "email": "string", "password": "string" }`
   - **Response:** `{ "token": "string", "user": UserDto }`

2. **POST /api/plans**
   - **Resource:** Study Plans
   - **Method:** POST
   - **Function:** Create new study plan with subjects and time allocations
   - **Request Body:** `{ "title": "string", "subjects": [SubjectDto], "deadline": "date" }`
   - **Response:** `{ "planId": "long", "status": "string" }`

3. **POST /api/sessions**
   - **Resource:** Study Sessions
   - **Method:** POST
   - **Function:** Log study session progress and trigger AI recommendations
   - **Request Body:** `{ "planId": "long", "subjectId": "long", "duration": "int", "completion": "double", "performance": "int" }`
   - **Response:** `{ "sessionId": "long", "recommendations": RecommendationDto }`

### Additional Supporting Endpoints
- `GET /api/plans/{userId}` - Retrieve user's study plans
- `GET /api/sessions/{planId}` - Get session history for a plan
- `PUT /api/plans/{planId}` - Update study plan based on AI recommendations

---

## 4. AI MVP Function

### Simple Recommendation Engine Mechanism

**Input Processing:**
- Analyzes logged session data (duration, completion rate, performance scores)
- Calculates average performance trends per subject
- Identifies subjects with declining performance or time allocation issues

**Core Algorithm:**
```java
public Recommendation generateRecommendation(List<StudySession> sessions, StudyPlan plan) {
    // Calculate performance metrics
    Map<Long, Double> subjectPerformance = calculateSubjectPerformance(sessions);
    Map<Long, Integer> timeAllocation = plan.getTimeAllocations();
    
    // Identify subjects needing adjustment
    for (Map.Entry<Long, Double> entry : subjectPerformance.entrySet()) {
        Long subjectId = entry.getKey();
        Double performance = entry.getValue();
        
        if (performance < 0.6) { // Below 60% performance threshold
            // Increase time allocation by 20%
            int currentTime = timeAllocation.get(subjectId);
            int newTime = (int) (currentTime * 1.2);
            
            return new Recommendation(
                RecommendationType.TIME_ADJUSTMENT,
                subjectId,
                newTime,
                "Increase study time due to performance below threshold"
            );
        }
    }
    
    return null; // No adjustments needed
}
```

**Output:**
- Single adaptive recommendation (time adjustment, difficulty modification, or schedule change)
- Confidence score (0-1)
- Reasoning explanation

---

## 5. Three-Sprint Development Plan

### Sprint 1: Data & API Foundation (Weeks 1-2)
**Backend Focus:**
- Set up PostgreSQL database schema
- Configure Spring Boot with JDBC connectivity
- Implement core data models (User, StudyPlan, StudySession, Recommendation)
- Create all essential REST API endpoints
- Implement basic authentication with JWT
- Set up database migrations and seed data

**Deliverables:**
- Complete backend API with CRUD operations
- Database schema and initial data
- API documentation (OpenAPI/Swagger)
- Basic unit tests for service layer

### Sprint 2: Frontend & Integration (Weeks 3-4)
**Frontend Focus:**
- Set up React.js application structure
- Create core components (Login, Dashboard, PlanCreation, SessionLogging)
- Implement state management and API integration
- Build responsive UI with modern design
- Integrate frontend with backend APIs
- Implement user authentication flow

**Deliverables:**
- Functional React SPA
- Complete user journey (Register → Login → Create Plan → Log Sessions)
- API integration with error handling
- Responsive design implementation

### Sprint 3: AI & Polish (Weeks 5-6)
**AI Integration Focus:**
- Implement Recommendation Engine in Spring Boot service layer
- Create AI recommendation processing logic
- Integrate AI recommendations with frontend display
- Implement end-to-end adaptive scheduling feature
- Performance optimization and testing
- Bug fixes and UI polish

**Deliverables:**
- Working AI recommendation system
- Complete end-to-end adaptive scheduling
- Performance testing and optimization
- Production-ready MVP

---

## 6. Database Schema Design

### Core Tables

**users**
- id (BIGINT, PRIMARY KEY)
- email (VARCHAR, UNIQUE)
- password_hash (VARCHAR)
- first_name (VARCHAR)
- last_name (VARCHAR)
- created_at (TIMESTAMP)

**study_plans**
- id (BIGINT, PRIMARY KEY)
- user_id (BIGINT, FOREIGN KEY)
- title (VARCHAR)
- description (TEXT)
- deadline (DATE)
- status (ENUM: ACTIVE, COMPLETED, PAUSED)
- created_at (TIMESTAMP)

**subjects**
- id (BIGINT, PRIMARY KEY)
- plan_id (BIGINT, FOREIGN KEY)
- name (VARCHAR)
- difficulty_level (ENUM: BEGINNER, INTERMEDIATE, ADVANCED)
- time_allocation_minutes (INT)
- current_progress_percent (DECIMAL)

**study_sessions**
- id (BIGINT, PRIMARY KEY)
- plan_id (BIGINT, FOREIGN KEY)
- subject_id (BIGINT, FOREIGN KEY)
- duration_minutes (INT)
- completion_percent (DECIMAL)
- performance_score (INT, 1-10)
- notes (TEXT)
- created_at (TIMESTAMP)

**recommendations**
- id (BIGINT, PRIMARY KEY)
- plan_id (BIGINT, FOREIGN KEY)
- subject_id (BIGINT, FOREIGN KEY)
- type (ENUM: TIME_ADJUSTMENT, DIFFICULTY_CHANGE, SCHEDULE_MODIFICATION)
- value (VARCHAR)
- confidence_score (DECIMAL)
- reasoning (TEXT)
- status (ENUM: PENDING, APPLIED, DISMISSED)
- created_at (TIMESTAMP)

---

## 7. Success Metrics

### Technical Metrics
- API response time < 200ms
- Frontend load time < 3 seconds
- 99% uptime for core features
- Zero critical security vulnerabilities

### User Experience Metrics
- User registration completion rate > 80%
- Study plan creation success rate > 90%
- Session logging completion rate > 85%
- AI recommendation acceptance rate > 60%

### Business Metrics
- User retention rate (7-day) > 40%
- Daily active users growth
- Feature adoption rates
- User feedback scores > 4.0/5.0

---

## 8. Risk Mitigation

### Technical Risks
- **Database Performance:** Implement proper indexing and query optimization
- **API Scalability:** Use connection pooling and caching strategies
- **Frontend Performance:** Implement code splitting and lazy loading

### Development Risks
- **Scope Creep:** Strict adherence to MVP feature set
- **Integration Issues:** Early and continuous integration testing
- **AI Complexity:** Start with simple algorithms, iterate based on feedback

### User Adoption Risks
- **Onboarding:** Clear user guides and intuitive UI
- **Value Proposition:** Focus on core adaptive scheduling benefits
- **Performance:** Ensure fast, responsive user experience

---

This MVP design provides a solid foundation for building a functional AI-backed study planner that can be rapidly developed, tested, and iterated upon based on user feedback.
