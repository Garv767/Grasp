@echo off
REM Grasp App Startup Script for Windows Command Prompt
REM This script starts all components in the correct sequence

echo 🚀 Starting Grasp Study Planner Application...
echo.

REM Step 1: Start MySQL
echo 🗄️ Starting MySQL database...
net start mysql
if %errorlevel% neq 0 (
    echo ❌ Failed to start MySQL. Please check if MySQL is installed.
    pause
    exit /b 1
)
echo ✅ MySQL started successfully
echo.

REM Step 2: Wait for MySQL to be ready
echo ⏳ Waiting for MySQL to be ready...
timeout /t 3 /nobreak >nul

REM Step 3: Setup database (first time only)
echo 🔧 Setting up database...
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS grasp_db; CREATE USER IF NOT EXISTS 'grasp_user'@'localhost' IDENTIFIED BY 'grasp_password'; GRANT ALL PRIVILEGES ON grasp_db.* TO 'grasp_user'@'localhost'; FLUSH PRIVILEGES;" 2>nul
mysql -u grasp_user -pgrasp_password grasp_db < database/schema.sql 2>nul
mysql -u grasp_user -pgrasp_password grasp_db < database/seed-data.sql 2>nul
echo ✅ Database setup complete
echo.

REM Step 4: Start Backend
echo 🔧 Starting Spring Boot backend...
start "Backend Server" cmd /k "cd backend && mvn spring-boot:run"
echo ✅ Backend startup initiated
echo.

REM Step 5: Wait for backend to be ready
echo ⏳ Waiting for backend to be ready...
timeout /t 15 /nobreak >nul

REM Step 6: Start Frontend
echo 🎨 Starting React frontend...
start "Frontend Server" cmd /k "cd frontend && npm start"
echo ✅ Frontend startup initiated
echo.

REM Step 7: Wait for frontend to be ready
echo ⏳ Waiting for frontend to be ready...
timeout /t 10 /nobreak >nul

REM Step 8: Final Status
echo 🎉 Grasp Study Planner is now running!
echo.
echo 📱 Access Points:
echo    Frontend:     http://localhost:3000
echo    Backend API:  http://localhost:8080
echo    API Docs:     http://localhost:8080/swagger-ui.html
echo.
echo 🔧 Development Tips:
echo    - Frontend auto-reloads on file changes
echo    - Backend requires restart for Java changes
echo    - Close terminal windows to stop services
echo.
echo 📚 For detailed instructions, see docs/Startup_Sequence_Guide.md

REM Open browser to frontend
timeout /t 2 /nobreak >nul
start http://localhost:3000

pause

