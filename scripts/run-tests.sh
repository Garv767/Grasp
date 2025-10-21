#!/bin/bash

# Grasp Study Planner - Test Execution Script
# This script runs all tests for the project

echo "🧪 Running Grasp Study Planner Tests..."

# Run backend tests
echo ""
echo "🔧 Running backend tests..."
cd backend
mvn test
if [ $? -ne 0 ]; then
    echo "❌ Backend tests failed!"
    exit 1
fi
echo "✅ Backend tests passed"

cd ..

# Run frontend tests
echo ""
echo "🎨 Running frontend tests..."
cd frontend
npm test -- --coverage --watchAll=false
if [ $? -ne 0 ]; then
    echo "❌ Frontend tests failed!"
    exit 1
fi
echo "✅ Frontend tests passed"

cd ..

echo ""
echo "🎉 All tests passed successfully!"
echo ""
echo "📊 Test coverage reports:"
echo "- Backend: Check target/site/jacoco/index.html"
echo "- Frontend: Check frontend/coverage/lcov-report/index.html"
