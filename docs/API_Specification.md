# API Specification - Grasp Study Planner

## Base URL
```
http://localhost:8080/api
```

## Current Implementation Status
✅ **Fully Implemented:** All core endpoints are functional
✅ **Database:** PostgreSQL 16 with complete schema
✅ **Authentication:** JWT-based security implemented
✅ **Frontend:** Modern React UI with dark/light themes
✅ **AI Engine:** Recommendation system operational

## Authentication
All endpoints (except login/register) require JWT authentication via Authorization header:
```
Authorization: Bearer <jwt_token>
```

---

## Critical API Endpoints

### 1. Authentication

#### POST /auth/login
**Purpose:** User login and JWT token generation

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

#### POST /auth/register
**Purpose:** User registration

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

### 2. Study Plans

#### POST /plans
**Purpose:** Create new study plan with subjects and time allocations

**Request Body:**
```json
{
  "title": "Java Programming Course",
  "description": "Complete Java programming fundamentals",
  "deadline": "2024-12-31",
  "subjects": [
    {
      "name": "Java Basics",
      "difficultyLevel": "BEGINNER",
      "timeAllocationMinutes": 1200
    },
    {
      "name": "Object-Oriented Programming",
      "difficultyLevel": "INTERMEDIATE",
      "timeAllocationMinutes": 1800
    }
  ]
}
```

**Response:**
```json
{
  "planId": 1,
  "status": "ACTIVE",
  "message": "Study plan created successfully"
}
```

#### GET /plans
**Purpose:** Retrieve user's study plans

**Response:**
```json
[
  {
    "id": 1,
    "title": "Java Programming Course",
    "description": "Complete Java programming fundamentals",
    "deadline": "2024-12-31",
    "status": "ACTIVE",
    "subjects": [
      {
        "id": 1,
        "name": "Java Basics",
        "difficultyLevel": "BEGINNER",
        "timeAllocationMinutes": 1200,
        "currentProgressPercent": 0.00
      }
    ],
    "createdAt": "2024-01-15T10:30:00Z"
  }
]
```

#### PUT /plans/{planId}
**Purpose:** Update study plan based on AI recommendations

**Request Body:**
```json
{
  "title": "Updated Java Programming Course",
  "subjects": [
    {
      "id": 1,
      "timeAllocationMinutes": 1440
    }
  ]
}
```

### 3. Study Sessions

#### POST /sessions
**Purpose:** Log study session progress and trigger AI recommendations

**Request Body:**
```json
{
  "planId": 1,
  "subjectId": 1,
  "durationMinutes": 60,
  "completionPercent": 75.5,
  "performanceScore": 8,
  "notes": "Focused on inheritance concepts"
}
```

**Response:**
```json
{
  "sessionId": 1,
  "recommendations": {
    "id": 1,
    "type": "TIME_ADJUSTMENT",
    "value": "1440",
    "confidenceScore": 0.85,
    "reasoning": "Performance below threshold, increase study time by 20%",
    "status": "PENDING"
  },
  "message": "Session logged successfully"
}
```

#### GET /sessions/{planId}
**Purpose:** Get session history for a plan

**Response:**
```json
[
  {
    "id": 1,
    "subjectId": 1,
    "subjectName": "Java Basics",
    "durationMinutes": 60,
    "completionPercent": 75.5,
    "performanceScore": 8,
    "notes": "Focused on inheritance concepts",
    "createdAt": "2024-01-15T14:30:00Z"
  }
]
```

### 4. Recommendations

#### GET /recommendations/{planId}
**Purpose:** Get AI recommendations for a study plan

**Response:**
```json
[
  {
    "id": 1,
    "subjectId": 1,
    "subjectName": "Java Basics",
    "type": "TIME_ADJUSTMENT",
    "value": "1440",
    "confidenceScore": 0.85,
    "reasoning": "Performance below threshold, increase study time by 20%",
    "status": "PENDING",
    "createdAt": "2024-01-15T14:35:00Z"
  }
]
```

#### PUT /recommendations/{recommendationId}/apply
**Purpose:** Apply an AI recommendation

**Response:**
```json
{
  "message": "Recommendation applied successfully",
  "updatedPlan": {
    "id": 1,
    "subjects": [
      {
        "id": 1,
        "timeAllocationMinutes": 1440
      }
    ]
  }
}
```

---

## Data Transfer Objects (DTOs)

### UserDto
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

### StudyPlanDto
```json
{
  "id": 1,
  "title": "Java Programming Course",
  "description": "Complete Java programming fundamentals",
  "deadline": "2024-12-31",
  "status": "ACTIVE",
  "subjects": [SubjectDto],
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### SubjectDto
```json
{
  "id": 1,
  "name": "Java Basics",
  "difficultyLevel": "BEGINNER",
  "timeAllocationMinutes": 1200,
  "currentProgressPercent": 0.00
}
```

### StudySessionDto
```json
{
  "id": 1,
  "subjectId": 1,
  "subjectName": "Java Basics",
  "durationMinutes": 60,
  "completionPercent": 75.5,
  "performanceScore": 8,
  "notes": "Focused on inheritance concepts",
  "createdAt": "2024-01-15T14:30:00Z"
}
```

### RecommendationDto
```json
{
  "id": 1,
  "subjectId": 1,
  "subjectName": "Java Basics",
  "type": "TIME_ADJUSTMENT",
  "value": "1440",
  "confidenceScore": 0.85,
  "reasoning": "Performance below threshold, increase study time by 20%",
  "status": "PENDING",
  "createdAt": "2024-01-15T14:35:00Z"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Invalid request data",
  "details": [
    {
      "field": "email",
      "message": "Email is required"
    }
  ]
}
```

### 401 Unauthorized
```json
{
  "error": "UNAUTHORIZED",
  "message": "Invalid or expired token"
}
```

### 404 Not Found
```json
{
  "error": "NOT_FOUND",
  "message": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "INTERNAL_ERROR",
  "message": "An unexpected error occurred"
}
```

---

## API Testing

### Using curl

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

**Create Study Plan:**
```bash
curl -X POST http://localhost:8080/api/plans \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"title":"Test Plan","subjects":[{"name":"Test Subject","timeAllocationMinutes":60}]}'
```

**Log Study Session:**
```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"planId":1,"subjectId":1,"durationMinutes":60,"completionPercent":75.5,"performanceScore":8}'
```

### Swagger UI
Access API documentation at: `http://localhost:8080/swagger-ui.html`

---

## 🎯 Complete Feature Overview

### **Core Application Features**

#### 1. **User Management**
- ✅ User registration with email validation
- ✅ Secure login with JWT authentication
- ✅ Password hashing with BCrypt
- ✅ Session management and token refresh
- ✅ User profile management

#### 2. **Study Plan Management**
- ✅ Create study plans with multiple subjects
- ✅ Set difficulty levels (BEGINNER, INTERMEDIATE, ADVANCED)
- ✅ Time allocation per subject
- ✅ Deadline tracking and status management
- ✅ Plan editing and updates

#### 3. **Session Tracking**
- ✅ Log study sessions with duration tracking
- ✅ Performance scoring (1-10 scale)
- ✅ Completion percentage tracking
- ✅ Session notes and observations
- ✅ Historical session data

#### 4. **AI Recommendation Engine**
- ✅ Performance analysis algorithms
- ✅ Time adjustment recommendations
- ✅ Difficulty level suggestions
- ✅ Schedule optimization tips
- ✅ Confidence scoring for recommendations

#### 5. **Progress Analytics**
- ✅ Visual progress indicators
- ✅ Study time statistics
- ✅ Performance trends
- ✅ Subject-wise progress tracking
- ✅ Achievement milestones

### **Modern UI Features**

#### 6. **Theme System**
- ✅ Dark/Light theme switching
- ✅ High contrast text for readability
- ✅ Smooth theme transitions
- ✅ Consistent color palette
- ✅ Accessibility-compliant contrast ratios

#### 7. **Interactive Dashboard**
- ✅ Real-time statistics cards
- ✅ Quick action buttons
- ✅ Recent activity feed
- ✅ Progress visualization
- ✅ Animated transitions

#### 8. **Responsive Design**
- ✅ Mobile-first approach
- ✅ Tablet and desktop optimization
- ✅ Touch-friendly interactions
- ✅ Adaptive layouts
- ✅ Cross-browser compatibility

#### 9. **Modern Design Elements**
- ✅ Glassmorphism effects
- ✅ Gradient backgrounds
- ✅ Smooth animations
- ✅ Hover effects and transitions
- ✅ Professional typography

### **Technical Features**

#### 10. **Backend Architecture**
- ✅ Spring Boot 3.2.0 framework
- ✅ RESTful API design
- ✅ JWT security implementation
- ✅ PostgreSQL database integration
- ✅ Comprehensive error handling

#### 11. **Database Features**
- ✅ PostgreSQL 16 with optimized schema
- ✅ Proper foreign key relationships
- ✅ Indexed queries for performance
- ✅ Data validation and constraints
- ✅ Seed data for development

#### 12. **API Features**
- ✅ Complete REST API endpoints
- ✅ Swagger/OpenAPI documentation
- ✅ Request/response validation
- ✅ Error handling and status codes
- ✅ CORS configuration

#### 13. **Frontend Features**
- ✅ React 18 with modern hooks
- ✅ Material-UI component library
- ✅ Context API for state management
- ✅ Axios for API communication
- ✅ React Router for navigation

#### 14. **Development Features**
- ✅ Docker containerization
- ✅ Docker Compose for local development
- ✅ Maven build system
- ✅ npm package management
- ✅ Hot reload for development

### **Security Features**

#### 15. **Authentication & Authorization**
- ✅ JWT token-based authentication
- ✅ Secure password storage
- ✅ Session management
- ✅ Protected route handling
- ✅ Token expiration handling

#### 16. **Data Security**
- ✅ Input validation and sanitization
- ✅ SQL injection prevention
- ✅ XSS protection
- ✅ CORS configuration
- ✅ Secure headers

### **Performance Features**

#### 17. **Optimization**
- ✅ Database query optimization
- ✅ Frontend code splitting
- ✅ Image optimization
- ✅ Caching strategies
- ✅ Lazy loading

#### 18. **Monitoring**
- ✅ Application health checks
- ✅ Error logging
- ✅ Performance metrics
- ✅ Debug information
- ✅ Development tools

### **Deployment Features**

#### 19. **Containerization**
- ✅ Docker container support
- ✅ Docker Compose configuration
- ✅ Multi-stage builds
- ✅ Environment configuration
- ✅ Production-ready setup

#### 20. **Documentation**
- ✅ Complete API documentation
- ✅ Development guides
- ✅ Setup instructions
- ✅ Architecture documentation
- ✅ User guides

---

## 🚀 Ready for Production

The Grasp Study Planner is now a **complete, production-ready application** with:

- ✅ **Full-stack implementation** (Frontend + Backend + Database)
- ✅ **Modern, beautiful UI** with dark/light themes
- ✅ **Complete feature set** for study planning and tracking
- ✅ **AI-powered recommendations** for adaptive learning
- ✅ **Professional codebase** with proper architecture
- ✅ **Comprehensive documentation** for development and deployment
- ✅ **Docker support** for easy deployment
- ✅ **PostgreSQL database** with optimized schema
- ✅ **Security implementation** with JWT authentication
- ✅ **Responsive design** that works on all devices

**The application is ready for immediate use and can be deployed to production!** 🎉
