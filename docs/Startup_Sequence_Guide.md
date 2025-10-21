# Grasp App Startup Sequence Guide

## 🚀 Complete Startup Process

Follow these steps in the exact order to start all components of the Grasp Study Planner application.

---

## Prerequisites Check

Before starting, ensure you have:
- ✅ Java 17+ installed
- ✅ Node.js 16+ installed  
- ✅ PostgreSQL 16+ installed
- ✅ Maven 3.6+ installed

---

## Step 1: Start PostgreSQL Database

### Option A: Using PostgreSQL Service (Recommended)
```bash
# Windows
net start postgresql-x64-16

# Linux/Mac
sudo service postgresql start
# or
sudo systemctl start postgresql
```

### Option B: Using Docker (Alternative)
```bash
# Start only PostgreSQL container
docker run --name grasp-postgres -e POSTGRES_DB=grasp_db -e POSTGRES_USER=grasp_user -e POSTGRES_PASSWORD=grasp_password -p 5432:5432 -d postgres:16
```

### Verify PostgreSQL is Running
```bash
# Test connection
psql -U postgres
# Enter password when prompted
# Type: \l (should see grasp_db)
# Type: \q
```

---

## Step 2: Setup Database Schema (First Time Only)

```bash
# Navigate to project root
cd C:\Users\DELL\Cooking\Grasp

# Create database and user (if not exists)
psql -U postgres -c "CREATE DATABASE grasp_db;"
psql -U postgres -c "CREATE USER grasp_user WITH PASSWORD 'grasp_password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE grasp_db TO grasp_user;"

# Import schema
psql -U grasp_user -d grasp_db -f database/schema.sql

# Import sample data
psql -U grasp_user -d grasp_db -f database/seed-data.sql
```

---

## Step 3: Start Backend (Spring Boot)

### Terminal 1: Backend Server
```bash
# Navigate to backend directory
cd backend

# Install dependencies (first time only)
mvn clean install

# Start Spring Boot application
mvn spring-boot:run
```

**Wait for this message:** `Started GraspApplication in X.XXX seconds`

**Backend will be available at:** `http://localhost:8080`

### Verify Backend is Running
```bash
# Test API health (in new terminal)
curl http://localhost:8080/api/auth/login
# Should return: {"error":"METHOD_NOT_ALLOWED","message":"Request method 'GET' not supported"}
```

---

## Step 4: Start Frontend (React)

### Terminal 2: Frontend Server
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies (first time only)
npm install

# Start React development server
npm start
```

**Wait for this message:** `webpack compiled successfully`

**Frontend will be available at:** `http://localhost:3000`

### Verify Frontend is Running
- Open browser and go to `http://localhost:3000`
- You should see the Grasp login page

---

## Step 5: Verify Complete Setup

### Test Complete Application Flow

1. **Open Browser:** Go to `http://localhost:3000`

2. **Register New User:**
   - Click "Register" or go to `/register`
   - Fill in: Email, Password, First Name, Last Name
   - Click "Register"

3. **Login:**
   - Use credentials from registration
   - Click "Login"

4. **Create Study Plan:**
   - Click "Create New Plan" or go to `/plans/new`
   - Fill in plan details and subjects
   - Click "Create Plan"

5. **Log Study Session:**
   - Click "Log Session" or go to `/sessions/log`
   - Select plan and subject
   - Fill in session details
   - Click "Log Session"

6. **View AI Recommendations:**
   - Go to `/recommendations`
   - View AI-generated suggestions

---

## 🛠️ Development Mode Startup (Daily)

For daily development, use this simplified sequence:

### Quick Start Script
```bash
# Terminal 1: Start PostgreSQL
net start postgresql-x64-16

# Terminal 2: Start Backend
cd backend && mvn spring-boot:run

# Terminal 3: Start Frontend  
cd frontend && npm start
```

### Or Use Docker Compose (All-in-One)
```bash
# Start everything with Docker
docker-compose up -d

# Check status
docker-compose ps
```

---

## 🔧 Troubleshooting Common Issues

### Backend Won't Start
```bash
# Check Java version
java -version
# Should be 17+

# Check if port 8080 is free
netstat -an | findstr :8080

# Kill process using port 8080 (if needed)
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F
```

### Frontend Won't Start
```bash
# Check Node version
node -v
# Should be 16+

# Check if port 3000 is free
netstat -an | findstr :3000

# Clear npm cache (if needed)
npm cache clean --force
rm -rf node_modules
npm install
```

### Database Connection Issues
```bash
# Check PostgreSQL service
net start postgresql-x64-16

# Test connection
psql -U grasp_user -d grasp_db

# Check if database exists
psql -U postgres -c "\l"
```

### API Connection Issues
```bash
# Test backend API
curl http://localhost:8080/api/auth/login

# Check CORS settings in backend
# Verify application.yml configuration
```

---

## 📊 Service Status Check

### Check All Services Running
```bash
# PostgreSQL
netstat -an | findstr :5432

# Backend API
netstat -an | findstr :8080

# Frontend
netstat -an | findstr :3000
```

### Access Points
- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **API Documentation:** http://localhost:8080/swagger-ui.html
- **Database:** localhost:5432

---

## 🚨 Emergency Restart Sequence

If something goes wrong, follow this restart sequence:

1. **Stop All Services:**
   ```bash
   # Stop frontend (Ctrl+C in Terminal 3)
   # Stop backend (Ctrl+C in Terminal 2)
   # Stop PostgreSQL
   net stop postgresql-x64-16
   ```

2. **Clean and Restart:**
   ```bash
   # Start PostgreSQL
   net start postgresql-x64-16
   
   # Restart backend
   cd backend
   mvn clean
   mvn spring-boot:run
   
   # Restart frontend (new terminal)
   cd frontend
   npm start
   ```

---

## 📝 Development Notes

### Hot Reload
- **Frontend:** Automatically reloads on file changes
- **Backend:** Restart required for Java changes
- **Database:** Schema changes require manual restart

### Logs Location
- **Backend:** Console output in Terminal 2
- **Frontend:** Console output in Terminal 3
- **Database:** PostgreSQL logs in PostgreSQL installation directory

### Performance Tips
- Keep PostgreSQL running between development sessions
- Use `mvn spring-boot:run` for faster backend startup
- Use `npm start` for frontend development mode

This sequence ensures all components start in the correct order and can communicate properly!

