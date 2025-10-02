#!/usr/bin/env python3
"""
AI-Backed Study Planner - ML Recommendation Service
Simple recommendation engine for MVP using decision trees
Authors: Garv Rahut & Shashwat Parashar
Version: 1.0.0-MVP
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
import numpy as np
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import StandardScaler
import json
import logging
from datetime import datetime, timedelta
import warnings
warnings.filterwarnings('ignore')

# Initialize Flask app
app = Flask(__name__)
CORS(app)

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Global model and scaler (in production, use proper model persistence)
model = None
scaler = None
is_model_trained = False

class StudyRecommendationEngine:
    def __init__(self):
        self.model = DecisionTreeClassifier(
            max_depth=5,
            min_samples_split=10,
            random_state=42
        )
        self.scaler = StandardScaler()
        self.is_trained = False
        
    def generate_mock_training_data(self, n_samples=1000):
        """Generate synthetic training data for MVP"""
        np.random.seed(42)
        
        data = []
        subjects = ['Mathematics', 'Physics', 'Chemistry', 'Biology', 'Computer Science', 'English']
        
        for _ in range(n_samples):
            subject_difficulty = np.random.uniform(0.2, 0.9)
            past_performance = np.random.uniform(0.1, 1.0)
            time_spent = np.random.uniform(30, 180)  # minutes
            completion_rate = np.random.uniform(0.3, 1.0)
            
            # Rule-based logic for time allocation
            if past_performance < 0.5 or completion_rate < 0.7:
                time_allocation = 2  # High priority
            elif past_performance > 0.8 and completion_rate > 0.9:
                time_allocation = 0  # Low priority
            else:
                time_allocation = 1  # Medium priority
                
            data.append([
                subject_difficulty,
                past_performance,
                time_spent,
                completion_rate,
                time_allocation
            ])
        
        columns = ['subject_difficulty', 'past_performance', 'time_spent', 'completion_rate', 'time_allocation']
        return pd.DataFrame(data, columns=columns)
    
    def train_model(self, training_data=None):
        """Train the recommendation model"""
        if training_data is None:
            training_data = self.generate_mock_training_data()
        
        X = training_data[['subject_difficulty', 'past_performance', 'time_spent', 'completion_rate']]
        y = training_data['time_allocation']
        
        # Scale features
        X_scaled = self.scaler.fit_transform(X)
        
        # Train model
        self.model.fit(X_scaled, y)
        self.is_trained = True
        
        logger.info("Model trained successfully with {} samples".format(len(training_data)))
        return True
    
    def get_recommendations(self, user_sessions):
        """Generate study recommendations based on user sessions"""
        if not self.is_trained:
            self.train_model()
        
        recommendations = {
            "recommendations": {
                "dailySchedule": [],
                "adaptationReason": "AI-based schedule optimization",
                "confidenceScore": 0.75
            }
        }
        
        # Process each subject
        subject_data = {}
        for session in user_sessions:
            subject = session.get('subject', 'Unknown')
            if subject not in subject_data:
                subject_data[subject] = {
                    'total_time': 0,
                    'session_count': 0,
                    'avg_performance': 0,
                    'avg_completion': 0,
                    'sessions': []
                }
            
            subject_data[subject]['sessions'].append(session)
            subject_data[subject]['total_time'] += session.get('duration', 0)
            subject_data[subject]['session_count'] += 1
        
        # Calculate averages and make predictions
        for subject, data in subject_data.items():
            if data['session_count'] > 0:
                avg_performance = np.mean([s.get('performanceScore', 0.5) for s in data['sessions']])
                avg_completion = np.mean([s.get('completionRate', 0.5) for s in data['sessions']])
                avg_time = data['total_time'] / data['session_count']
                
                # Estimate subject difficulty (simplified)
                difficulty_ratings = [s.get('difficultyRating', 3) for s in data['sessions'] if s.get('difficultyRating')]
                subject_difficulty = np.mean(difficulty_ratings) / 5.0 if difficulty_ratings else 0.5
                
                # Make prediction
                features = np.array([[subject_difficulty, avg_performance, avg_time, avg_completion]])
                features_scaled = self.scaler.transform(features)
                priority_prediction = self.model.predict(features_scaled)[0]
                
                # Map prediction to recommendation
                priority_map = {0: "low", 1: "medium", 2: "high"}
                time_map = {0: 45, 1: 75, 2: 120}  # minutes
                
                priority = priority_map.get(priority_prediction, "medium")
                recommended_time = time_map.get(priority_prediction, 75)
                
                # Generate reason
                reason = self._generate_reason(avg_performance, avg_completion, priority)
                
                recommendations["recommendations"]["dailySchedule"].append({
                    "subject": subject,
                    "priority": priority,
                    "recommendedTime": recommended_time,
                    "reason": reason,
                    "currentPerformance": round(avg_performance, 2),
                    "currentCompletion": round(avg_completion, 2)
                })
        
        # Sort by priority (high first)
        priority_order = {"high": 0, "medium": 1, "low": 2}
        recommendations["recommendations"]["dailySchedule"].sort(
            key=lambda x: priority_order.get(x["priority"], 1)
        )
        
        return recommendations
    
    def _generate_reason(self, performance, completion, priority):
        """Generate human-readable reason for recommendation"""
        if priority == "high":
            if performance < 0.6:
                return f"Low performance score ({performance:.0%}) - needs more focus"
            elif completion < 0.7:
                return f"Low completion rate ({completion:.0%}) - requires additional time"
            else:
                return "Important subject requiring consistent attention"
        elif priority == "low":
            if performance > 0.8 and completion > 0.9:
                return f"Excellent progress ({performance:.0%} performance) - maintenance mode"
            else:
                return "Good progress - reduced time allocation"
        else:
            return "Steady progress - maintaining current schedule"

# Initialize recommendation engine
recommendation_engine = StudyRecommendationEngine()

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        "status": "healthy",
        "service": "ML Recommendation Service",
        "version": "1.0.0-MVP",
        "model_trained": recommendation_engine.is_trained,
        "timestamp": datetime.now().isoformat()
    })

@app.route('/api/v1/train', methods=['POST'])
def train_model():
    """Train the ML model with provided data or mock data"""
    try:
        data = request.json
        training_data = data.get('training_data') if data else None
        
        success = recommendation_engine.train_model(training_data)
        
        return jsonify({
            "success": True,
            "message": "Model trained successfully",
            "model_trained": recommendation_engine.is_trained,
            "timestamp": datetime.now().isoformat()
        })
        
    except Exception as e:
        logger.error(f"Training failed: {str(e)}")
        return jsonify({
            "success": False,
            "message": f"Training failed: {str(e)}",
            "timestamp": datetime.now().isoformat()
        }), 500

@app.route('/api/v1/recommendations', methods=['POST'])
def get_recommendations():
    """Generate study recommendations based on user session data"""
    try:
        data = request.json
        user_sessions = data.get('sessions', [])
        user_id = data.get('userId')
        
        if not user_sessions:
            return jsonify({
                "success": False,
                "message": "No session data provided",
                "timestamp": datetime.now().isoformat()
            }), 400
        
        recommendations = recommendation_engine.get_recommendations(user_sessions)
        
        return jsonify({
            "success": True,
            "message": "Recommendations generated successfully",
            "userId": user_id,
            "data": recommendations,
            "timestamp": datetime.now().isoformat()
        })
        
    except Exception as e:
        logger.error(f"Recommendation generation failed: {str(e)}")
        return jsonify({
            "success": False,
            "message": f"Failed to generate recommendations: {str(e)}",
            "timestamp": datetime.now().isoformat()
        }), 500

@app.route('/api/v1/model-info', methods=['GET'])
def get_model_info():
    """Get information about the current model"""
    return jsonify({
        "model_type": "Decision Tree Classifier",
        "is_trained": recommendation_engine.is_trained,
        "features": ["subject_difficulty", "past_performance", "time_spent", "completion_rate"],
        "target": "time_allocation (0=low, 1=medium, 2=high priority)",
        "version": "1.0.0-MVP",
        "timestamp": datetime.now().isoformat()
    })

if __name__ == '__main__':
    # Auto-train model on startup with mock data
    logger.info("Starting ML Recommendation Service...")
    recommendation_engine.train_model()
    logger.info("Model auto-trained with synthetic data")
    
    # Start Flask server
    app.run(host='0.0.0.0', port=5000, debug=True)