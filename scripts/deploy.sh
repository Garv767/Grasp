#!/bin/bash

# Grasp Study Planner - Deployment Script
# This script builds and deploys the application

echo "🚀 Deploying Grasp Study Planner..."

# Build backend
echo ""
echo "🔧 Building backend..."
cd backend
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Backend build failed!"
    exit 1
fi
echo "✅ Backend built successfully"

cd ..

# Build frontend
echo ""
echo "🎨 Building frontend..."
cd frontend
npm run build
if [ $? -ne 0 ]; then
    echo "❌ Frontend build failed!"
    exit 1
fi
echo "✅ Frontend built successfully"

cd ..

# Create deployment package
echo ""
echo "📦 Creating deployment package..."
mkdir -p deploy
cp backend/target/*.jar deploy/
cp -r frontend/build deploy/frontend
cp docker/docker-compose.yml deploy/
cp database/schema.sql deploy/

echo "✅ Deployment package created in deploy/ directory"

echo ""
echo "🎉 Deployment preparation complete!"
echo ""
echo "📝 To deploy:"
echo "1. Copy the deploy/ directory to your server"
echo "2. Set up MySQL database on the server"
echo "3. Configure environment variables"
echo "4. Run: docker-compose up -d"
echo ""
echo "📚 For production deployment details, see docs/Development_Guide.md"
