#!/bin/bash

# Grasp Study Planner - Development Environment Setup Script
# This script sets up the complete development environment

echo "🚀 Setting up Grasp Study Planner Development Environment..."

# Check prerequisites
echo "📋 Checking prerequisites..."

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17+ and try again."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java version $JAVA_VERSION is too old. Please install Java 17+ and try again."
    exit 1
fi
echo "✅ Java $JAVA_VERSION found"

# Check Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 16+ and try again."
    exit 1
fi

NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 16 ]; then
    echo "❌ Node.js version $NODE_VERSION is too old. Please install Node.js 16+ and try again."
    exit 1
fi
echo "✅ Node.js $(node -v) found"

# Check MySQL
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL is not installed. Please install MySQL 8.0+ and try again."
    exit 1
fi
echo "✅ MySQL found"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6+ and try again."
    exit 1
fi
echo "✅ Maven $(mvn -version | head -n 1 | cut -d' ' -f3) found"

echo ""
echo "🔧 Setting up backend..."

# Install backend dependencies
cd backend
mvn clean install
if [ $? -ne 0 ]; then
    echo "❌ Backend setup failed. Please check the error messages above."
    exit 1
fi
echo "✅ Backend dependencies installed"

cd ..

echo ""
echo "🎨 Setting up frontend..."

# Install frontend dependencies
cd frontend
npm install
if [ $? -ne 0 ]; then
    echo "❌ Frontend setup failed. Please check the error messages above."
    exit 1
fi
echo "✅ Frontend dependencies installed"

cd ..

echo ""
echo "🗄️ Setting up database..."

# Create database and import schema
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS grasp_db;"
mysql -u root -p -e "CREATE USER IF NOT EXISTS 'grasp_user'@'localhost' IDENTIFIED BY 'grasp_password';"
mysql -u root -p -e "GRANT ALL PRIVILEGES ON grasp_db.* TO 'grasp_user'@'localhost';"
mysql -u root -p -e "FLUSH PRIVILEGES;"

mysql -u grasp_user -pgrasp_password grasp_db < database/schema.sql
mysql -u grasp_user -pgrasp_password grasp_db < database/seed-data.sql

echo "✅ Database setup complete"

echo ""
echo "🎉 Development environment setup complete!"
echo ""
echo "📝 Next steps:"
echo "1. Start MySQL service: sudo service mysql start (Linux) or net start mysql (Windows)"
echo "2. Start backend: cd backend && mvn spring-boot:run"
echo "3. Start frontend: cd frontend && npm start"
echo ""
echo "🌐 Access points:"
echo "- Frontend: http://localhost:3000"
echo "- Backend API: http://localhost:8080"
echo "- API Documentation: http://localhost:8080/swagger-ui.html"
echo ""
echo "📚 For detailed instructions, see docs/Development_Guide.md"
