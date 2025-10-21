# Grasp App Startup Script for Windows PowerShell
# This script starts all components in the correct sequence

Write-Host "🚀 Starting Grasp Study Planner Application..." -ForegroundColor Green
Write-Host ""

# Function to check if a port is in use
function Test-Port {
    param([int]$Port)
    $connection = Get-NetTCPConnection -LocalPort $Port -ErrorAction SilentlyContinue
    return $connection -ne $null
}

# Function to wait for service to be ready
function Wait-ForService {
    param([string]$Url, [string]$ServiceName)
    Write-Host "⏳ Waiting for $ServiceName to be ready..." -ForegroundColor Yellow
    $maxAttempts = 30
    $attempt = 0
    
    do {
        try {
            $response = Invoke-WebRequest -Uri $Url -Method GET -TimeoutSec 5 -ErrorAction SilentlyContinue
            if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 405) {
                Write-Host "✅ $ServiceName is ready!" -ForegroundColor Green
                return $true
            }
        }
        catch {
            # Service not ready yet
        }
        
        Start-Sleep -Seconds 2
        $attempt++
        Write-Host "   Attempt $attempt/$maxAttempts..." -ForegroundColor Gray
    } while ($attempt -lt $maxAttempts)
    
    Write-Host "❌ $ServiceName failed to start within timeout" -ForegroundColor Red
    return $false
}

# Step 1: Check prerequisites
Write-Host "📋 Checking prerequisites..." -ForegroundColor Cyan

# Check Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.Line.Split('"')[1] }
    Write-Host "✅ Java $javaVersion found" -ForegroundColor Green
} catch {
    Write-Host "❌ Java not found. Please install Java 17+" -ForegroundColor Red
    exit 1
}

# Check Node.js
try {
    $nodeVersion = node -v
    Write-Host "✅ Node.js $nodeVersion found" -ForegroundColor Green
} catch {
    Write-Host "❌ Node.js not found. Please install Node.js 16+" -ForegroundColor Red
    exit 1
}

# Check MySQL
try {
    $mysqlStatus = Get-Service -Name "mysql" -ErrorAction SilentlyContinue
    if ($mysqlStatus) {
        Write-Host "✅ MySQL service found" -ForegroundColor Green
    } else {
        Write-Host "❌ MySQL service not found. Please install MySQL 8.0+" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ MySQL not found. Please install MySQL 8.0+" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 2: Start MySQL
Write-Host "🗄️ Starting MySQL database..." -ForegroundColor Cyan
try {
    Start-Service -Name "mysql" -ErrorAction Stop
    Write-Host "✅ MySQL started successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Failed to start MySQL: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Wait for MySQL to be ready
Start-Sleep -Seconds 3

# Step 3: Setup database (first time only)
Write-Host "🔧 Setting up database..." -ForegroundColor Cyan
try {
    # Check if database exists
    $dbExists = mysql -u root -p -e "SHOW DATABASES LIKE 'grasp_db';" 2>$null | Select-String "grasp_db"
    
    if (-not $dbExists) {
        Write-Host "📊 Creating database and importing schema..." -ForegroundColor Yellow
        
        # Create database and user
        mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS grasp_db; CREATE USER IF NOT EXISTS 'grasp_user'@'localhost' IDENTIFIED BY 'grasp_password'; GRANT ALL PRIVILEGES ON grasp_db.* TO 'grasp_user'@'localhost'; FLUSH PRIVILEGES;" 2>$null
        
        # Import schema
        mysql -u grasp_user -pgrasp_password grasp_db < database/schema.sql 2>$null
        
        # Import sample data
        mysql -u grasp_user -pgrasp_password grasp_db < database/seed-data.sql 2>$null
        
        Write-Host "✅ Database setup complete" -ForegroundColor Green
    } else {
        Write-Host "✅ Database already exists" -ForegroundColor Green
    }
} catch {
    Write-Host "⚠️ Database setup failed, but continuing..." -ForegroundColor Yellow
}

Write-Host ""

# Step 4: Start Backend
Write-Host "🔧 Starting Spring Boot backend..." -ForegroundColor Cyan

# Check if backend is already running
if (Test-Port -Port 8080) {
    Write-Host "⚠️ Port 8080 is already in use. Backend might already be running." -ForegroundColor Yellow
} else {
    # Start backend in new window
    $backendScript = @"
cd backend
Write-Host "🔧 Starting Spring Boot backend..." -ForegroundColor Cyan
mvn spring-boot:run
"@
    
    Start-Process powershell -ArgumentList "-NoExit", "-Command", $backendScript
    Write-Host "✅ Backend startup initiated" -ForegroundColor Green
}

# Wait for backend to be ready
if (-not (Wait-ForService -Url "http://localhost:8080/api/auth/login" -ServiceName "Backend API")) {
    Write-Host "❌ Backend failed to start properly" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 5: Start Frontend
Write-Host "🎨 Starting React frontend..." -ForegroundColor Cyan

# Check if frontend is already running
if (Test-Port -Port 3000) {
    Write-Host "⚠️ Port 3000 is already in use. Frontend might already be running." -ForegroundColor Yellow
} else {
    # Start frontend in new window
    $frontendScript = @"
cd frontend
Write-Host "🎨 Starting React frontend..." -ForegroundColor Cyan
npm start
"@
    
    Start-Process powershell -ArgumentList "-NoExit", "-Command", $frontendScript
    Write-Host "✅ Frontend startup initiated" -ForegroundColor Green
}

# Wait for frontend to be ready
if (-not (Wait-ForService -Url "http://localhost:3000" -ServiceName "React Frontend")) {
    Write-Host "❌ Frontend failed to start properly" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 6: Final Status
Write-Host "🎉 Grasp Study Planner is now running!" -ForegroundColor Green
Write-Host ""
Write-Host "📱 Access Points:" -ForegroundColor Cyan
Write-Host "   Frontend:     http://localhost:3000" -ForegroundColor White
Write-Host "   Backend API:  http://localhost:8080" -ForegroundColor White
Write-Host "   API Docs:     http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host ""
Write-Host "🔧 Development Tips:" -ForegroundColor Cyan
Write-Host "   - Frontend auto-reloads on file changes" -ForegroundColor White
Write-Host "   - Backend requires restart for Java changes" -ForegroundColor White
Write-Host "   - Use Ctrl+C in terminal windows to stop services" -ForegroundColor White
Write-Host ""
Write-Host "📚 For detailed instructions, see docs/Startup_Sequence_Guide.md" -ForegroundColor Gray

# Open browser to frontend
Start-Sleep -Seconds 2
Start-Process "http://localhost:3000"

