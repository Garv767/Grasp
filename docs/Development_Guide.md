# Development Guide - Grasp Study Planner

## Prerequisites

### Required Software
- **Java 17+** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
- **Node.js 16+** - Download from [nodejs.org](https://nodejs.org/)
- **PostgreSQL 16+** - Download from [postgresql.org](https://www.postgresql.org/download/)
- **Maven 3.6+** - Download from [maven.apache.org](https://maven.apache.org/download.cgi)
- **Git** - Download from [git-scm.com](https://git-scm.com/)

### IDE Recommendations
- **IntelliJ IDEA** (Community or Ultimate) for backend development
- **Visual Studio Code** with React extensions for frontend development
- **pgAdmin** for database management

---

## Development Environment Setup

### 1. Clone and Initialize Project
```bash
git clone <repository-url> Grasp
cd Grasp
```

### 2. Database Setup
```bash
# Start PostgreSQL service (Windows)
net start postgresql-x64-16

# Create database and user
psql -U postgres
```

```sql
CREATE DATABASE grasp_db;
CREATE USER grasp_user WITH PASSWORD 'grasp_password';
GRANT ALL PRIVILEGES ON DATABASE grasp_db TO grasp_user;
\q
```

```bash
# Import schema
psql -U grasp_user -d grasp_db -f database/schema.sql

# Import sample data
psql -U grasp_user -d grasp_db -f database/seed-data.sql
```

### 3. Backend Setup
```bash
cd backend

# Install dependencies
mvn clean install

# Run application
mvn spring-boot:run
```

**Backend will be available at:** `http://localhost:8080`

### 4. Frontend Setup
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

**Frontend will be available at:** `http://localhost:3000`

---

## Development Workflow

### Daily Development Process

1. **Start Development Environment**
   ```bash
   # Terminal 1: Start PostgreSQL
   net start postgresql-x64-16
   
   # Terminal 2: Start Backend
   cd backend && mvn spring-boot:run
   
   # Terminal 3: Start Frontend
   cd frontend && npm start
   ```

2. **Database Changes**
   - Create migration files in `database/migrations/`
   - Update schema.sql for major changes
   - Test migrations before committing

3. **API Development**
   - Follow RESTful conventions
   - Update API documentation in `docs/API_Specification.md`
   - Test endpoints using Swagger UI: `http://localhost:8080/swagger-ui.html`

4. **Frontend Development**
   - Create components in `frontend/src/components/`
   - Use Material-UI for consistent styling
   - Implement responsive design

---

## Three-Sprint Development Plan

### Sprint 1: Data & API Foundation (Weeks 1-2)

#### Week 1: Database & Core Models
**Backend Tasks:**
- [ ] Complete database schema implementation
- [ ] Create all entity models (User, StudyPlan, Subject, StudySession, Recommendation)
- [ ] Implement JDBC repositories
- [ ] Set up database migrations
- [ ] Create seed data for development

**Deliverables:**
- Working database with all tables
- Basic CRUD operations for all entities
- Database connection testing

#### Week 2: REST API Implementation
**Backend Tasks:**
- [ ] Implement authentication endpoints (login/register)
- [ ] Create study plan management endpoints
- [ ] Implement session logging endpoints
- [ ] Add JWT security configuration
- [ ] Create API documentation with Swagger

**Deliverables:**
- Complete REST API with all endpoints
- JWT authentication working
- API documentation available
- Unit tests for service layer

**Sprint 1 Success Criteria:**
- All API endpoints responding correctly
- Database operations working
- Authentication flow complete
- API documentation accessible

### Sprint 2: Frontend & Integration (Weeks 3-4)

#### Week 3: React Components & UI
**Frontend Tasks:**
- [ ] Set up React application structure
- [ ] Create authentication components (Login, Register)
- [ ] Build dashboard with study plan overview
- [ ] Implement study plan creation form
- [ ] Create session logging interface
- [ ] Add responsive design with Material-UI

**Deliverables:**
- Complete React SPA structure
- All major UI components created
- Responsive design implemented
- Navigation and routing working

#### Week 4: API Integration & User Journey
**Frontend Tasks:**
- [ ] Implement API service layer with Axios
- [ ] Connect authentication flow to backend
- [ ] Integrate study plan creation with API
- [ ] Connect session logging to backend
- [ ] Add error handling and loading states
- [ ] Implement user state management

**Deliverables:**
- Complete user journey working end-to-end
- API integration complete
- Error handling implemented
- User can register, login, create plans, log sessions

**Sprint 2 Success Criteria:**
- Complete user registration and login flow
- Users can create and manage study plans
- Session logging functionality working
- Frontend communicates successfully with backend

### Sprint 3: AI & Polish (Weeks 5-6)

#### Week 5: AI Recommendation Engine
**Backend Tasks:**
- [ ] Implement RecommendationEngineService
- [ ] Create recommendation generation logic
- [ ] Add recommendation endpoints
- [ ] Implement recommendation application logic
- [ ] Test AI engine with sample data

**Frontend Tasks:**
- [ ] Create recommendation display components
- [ ] Implement recommendation acceptance/rejection
- [ ] Add progress visualization
- [ ] Create performance analytics dashboard

**Deliverables:**
- Working AI recommendation system
- Recommendations displayed in frontend
- Users can apply or dismiss recommendations
- Basic analytics and progress tracking

#### Week 6: Testing, Optimization & Polish
**Testing Tasks:**
- [ ] End-to-end testing of complete user journey
- [ ] Performance testing and optimization
- [ ] Security testing and validation
- [ ] Cross-browser compatibility testing
- [ ] Mobile responsiveness testing

**Polish Tasks:**
- [ ] UI/UX improvements based on testing
- [ ] Error message improvements
- [ ] Loading state enhancements
- [ ] Code cleanup and documentation
- [ ] Deployment preparation

**Deliverables:**
- Production-ready MVP
- Complete test coverage
- Performance optimized
- Deployment ready

**Sprint 3 Success Criteria:**
- AI recommendations working correctly
- Complete end-to-end functionality
- Performance meets requirements
- Ready for user testing and feedback

---

## Testing Strategy

### Backend Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=RecommendationEngineServiceTest

# Run integration tests
mvn test -Dtest=*IntegrationTest
```

### Frontend Testing
```bash
# Run all tests
npm test

# Run tests in watch mode
npm test -- --watch

# Run tests with coverage
npm test -- --coverage
```

### API Testing
```bash
# Test authentication
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Test study plan creation
curl -X POST http://localhost:8080/api/plans \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"title":"Test Plan","subjects":[{"name":"Test Subject","timeAllocationMinutes":60}]}'
```

---

## Code Standards

### Backend (Java/Spring Boot)
- Follow Spring Boot conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Implement proper error handling
- Use validation annotations
- Follow RESTful API design principles

### Frontend (React)
- Use functional components with hooks
- Implement proper error boundaries
- Use TypeScript for type safety (optional)
- Follow Material-UI design patterns
- Implement responsive design
- Use proper state management

### Database
- Use descriptive table and column names
- Implement proper foreign key constraints
- Add appropriate indexes for performance
- Use consistent naming conventions
- Document schema changes

---

## Deployment

### Development Deployment
```bash
# Using Docker Compose
docker-compose up -d

# Manual deployment
# Backend
cd backend && mvn clean package
java -jar target/grasp-backend-1.0.0.jar

# Frontend
cd frontend && npm run build
# Serve build folder with nginx or similar
```

### Production Considerations
- Use environment variables for configuration
- Implement proper logging
- Set up monitoring and alerting
- Configure database backups
- Implement CI/CD pipeline
- Use HTTPS in production

---

## Troubleshooting

### Common Issues

**Backend won't start:**
- Check Java version (must be 17+)
- Verify PostgreSQL is running
- Check database connection settings
- Review application logs

**Frontend won't start:**
- Check Node.js version (must be 16+)
- Delete node_modules and run `npm install`
- Check for port conflicts (3000)
- Verify proxy settings in package.json

**Database connection issues:**
- Verify PostgreSQL service is running
- Check username/password in application.yml
- Ensure database exists
- Check firewall settings

**API calls failing:**
- Verify backend is running on port 8080
- Check CORS configuration
- Verify JWT token validity
- Review network tab in browser dev tools

---

## Resources

### Documentation
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://reactjs.org/docs)
- [Material-UI Documentation](https://mui.com/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

### Tools
- [Postman](https://www.postman.com/) - API testing
- [pgAdmin](https://www.pgadmin.org/) - Database management
- [React Developer Tools](https://chrome.google.com/webstore/detail/react-developer-tools) - Browser extension

This development guide provides a comprehensive roadmap for building the Grasp Study Planner MVP efficiently and effectively.
