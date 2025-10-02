# AI-Backed Study Planner MVP

**Project Leaders:** Garv Rahut & Shashwat Parashar

## Overview
A cross-platform AI-powered study planner that provides personalized, adaptive scheduling based on user progress and performance analytics.

## Project Structure
```
study-planner-mvp/
├── backend/          # Java Spring Boot REST API
├── frontend/         # JSP/Thymeleaf Web Application  
├── mobile/          # Android Application
├── ml-service/      # Python ML Recommendation Engine
├── database/        # MySQL Schema and Scripts
├── docs/           # Documentation and Design
└── README.md       # This file
```

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Python 3.9+
- Android Studio (for mobile)

### Backend Setup
```bash
cd backend
mvn spring-boot:run
```

### Frontend Setup
The web frontend is integrated with the Spring Boot backend using JSP/Thymeleaf templates.

### ML Service Setup
```bash
cd ml-service
pip install -r requirements.txt
python app.py
```

## Features (MVP)
1. **Study Plan Creation & Management** - Create and manage personalized study schedules
2. **Progress Tracking & Analytics** - Log study sessions and view progress dashboards  
3. **AI-Powered Schedule Adaptation** - Automatic schedule adjustments based on performance
4. **User Authentication** - Secure user registration and login
5. **Cross-Platform Access** - Web and mobile interfaces

## API Documentation
Once running, visit: `http://localhost:8080/swagger-ui.html`

## Development Status
🚧 **MVP Development in Progress** 🚧

See `docs/MVP_Design_Document.md` for detailed specifications.