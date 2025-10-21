# Grasp - AI-Backed Study Planner and Progress Tracker

## Project Structure

```
Grasp/
├── docs/                           # Documentation
│   ├── MVP_Design_Document.md     # Complete MVP technical design
│   ├── API_Specification.md       # Detailed API documentation
│   └── Development_Guide.md       # Setup and development instructions
├── frontend/                      # React.js SPA
│   ├── public/
│   ├── src/
│   │   ├── components/            # React components
│   │   ├── services/              # API service layer
│   │   ├── context/               # React Context for state management
│   │   ├── utils/                 # Utility functions
│   │   └── App.js
│   ├── package.json
│   └── README.md
├── backend/                       # Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/grasp/
│   │   │   │   ├── controller/    # REST Controllers
│   │   │   │   ├── service/       # Business logic and AI engine
│   │   │   │   ├── repository/    # Data access layer
│   │   │   │   ├── model/         # Entity models
│   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   ├── config/        # Configuration classes
│   │   │   │   └── GraspApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/  # Database migrations
│   │   └── test/                  # Unit and integration tests
│   ├── pom.xml
│   └── README.md
├── database/                      # Database scripts and migrations
│   ├── schema.sql                 # Initial database schema
│   ├── seed-data.sql              # Sample data for development
│   └── migrations/                # Version-controlled migrations
├── docker/                        # Docker configuration
│   ├── docker-compose.yml         # Local development environment
│   └── Dockerfile.backend         # Backend container
├── scripts/                       # Development and deployment scripts
│   ├── setup-dev.sh              # Development environment setup
│   ├── run-tests.sh              # Test execution script
│   └── deploy.sh                 # Deployment script
└── README.md                      # Project overview and quick start
```

## Quick Start

1. **Prerequisites**
   - Java 17+
   - Node.js 16+
   - PostgreSQL 16+
   - Maven 3.6+

2. **Development Setup**
   ```bash
   # Clone and setup
   git clone <repository-url> Grasp
   cd Grasp
   
   # Run setup script
   ./scripts/setup-dev.sh
   
   # Start development environment
   docker-compose up -d
   
   # Start backend
   cd backend && mvn spring-boot:run
   
   # Start frontend (in new terminal)
   cd frontend && npm start
   ```

3. **Access Points**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Database: localhost:5432 (grasp_db)

## Technology Stack

- **Frontend:** React.js, Material-UI, Axios, Modern UI with Dark/Light Themes
- **Backend:** Java Spring Boot, Spring Security, Spring Data JDBC
- **Database:** PostgreSQL 16
- **AI/ML:** Custom Java-based recommendation engine
- **Build Tools:** Maven, npm
- **Containerization:** Docker, Docker Compose

## Key Features

### 🎯 Core Functionality
- **User Authentication:** Secure login/register with JWT tokens
- **Study Plan Management:** Create and manage study plans with multiple subjects
- **Session Logging:** Track study sessions with performance metrics
- **AI Recommendations:** Smart suggestions for time allocation and difficulty adjustments
- **Progress Tracking:** Visual progress indicators and analytics

### 🎨 Modern UI Features
- **Dark/Light Theme:** Beautiful theme switching with excellent contrast
- **Responsive Design:** Works perfectly on all device sizes
- **Glassmorphism Design:** Modern translucent cards with backdrop blur
- **Smooth Animations:** Fluid transitions and hover effects
- **Interactive Dashboard:** Real-time stats and activity feed

### 🚀 Technical Features
- **RESTful API:** Complete REST API with Swagger documentation
- **PostgreSQL Database:** Robust data storage with proper relationships
- **Spring Security:** JWT-based authentication and authorization
- **Docker Support:** Easy deployment with Docker Compose
- **Modern Frontend:** React with Material-UI and custom theming

## Development Phases

- **Sprint 1:** Data & API Foundation ✅
- **Sprint 2:** Frontend & Integration ✅  
- **Sprint 3:** AI & Polish ✅

See `docs/MVP_Design_Document.md` for complete technical specifications.
