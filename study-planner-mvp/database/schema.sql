-- AI-Backed Study Planner Database Schema
-- MVP Version 1.0.0
-- Authors: Garv Rahut & Shashwat Parashar

-- Create database
CREATE DATABASE IF NOT EXISTS study_planner_db;
USE study_planner_db;

-- Create user for application (run as root)
CREATE USER IF NOT EXISTS 'study_user'@'localhost' IDENTIFIED BY 'study_password';
GRANT ALL PRIVILEGES ON study_planner_db.* TO 'study_user'@'localhost';
FLUSH PRIVILEGES;

-- =====================================
-- User Management Tables
-- =====================================

-- Users table for authentication and profiles
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    study_preferences JSON,
    timezone VARCHAR(50) DEFAULT 'UTC',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_created_date (created_date)
);

-- =====================================
-- Study Planning Tables  
-- =====================================

-- Study plans created by users
CREATE TABLE study_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    subjects JSON NOT NULL, -- Array of subject names
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    daily_hours DECIMAL(4,2) NOT NULL DEFAULT 2.0,
    difficulty_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'INTERMEDIATE',
    status ENUM('DRAFT', 'ACTIVE', 'COMPLETED', 'PAUSED') DEFAULT 'DRAFT',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_dates (start_date, end_date)
);

-- =====================================
-- Progress Tracking Tables
-- =====================================

-- Individual study sessions logged by users
CREATE TABLE study_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    study_plan_id BIGINT,
    subject VARCHAR(100) NOT NULL,
    topic VARCHAR(200),
    duration_minutes INT NOT NULL,
    performance_score DECIMAL(3,2) CHECK (performance_score >= 0 AND performance_score <= 1),
    completion_rate DECIMAL(3,2) CHECK (completion_rate >= 0 AND completion_rate <= 1),
    difficulty_rating TINYINT CHECK (difficulty_rating >= 1 AND difficulty_rating <= 5),
    notes TEXT,
    session_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    mood_rating TINYINT CHECK (mood_rating >= 1 AND mood_rating <= 5),
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (study_plan_id) REFERENCES study_plans(id) ON DELETE SET NULL,
    INDEX idx_user_subject (user_id, subject),
    INDEX idx_session_date (session_date),
    INDEX idx_performance (performance_score)
);

-- =====================================
-- AI Recommendation Tables
-- =====================================

-- AI-generated recommendations for users
CREATE TABLE recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    study_plan_id BIGINT,
    recommendations_json JSON NOT NULL,
    recommendation_type ENUM('SCHEDULE_ADJUSTMENT', 'DIFFICULTY_CHANGE', 'TIME_ALLOCATION', 'SUBJECT_PRIORITY') NOT NULL,
    confidence_score DECIMAL(3,2) CHECK (confidence_score >= 0 AND confidence_score <= 1),
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'EXPIRED') DEFAULT 'PENDING',
    applied_date TIMESTAMP NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 7 DAY),
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (study_plan_id) REFERENCES study_plans(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_date (created_date),
    INDEX idx_confidence (confidence_score)
);

-- =====================================
-- Analytics and Metrics Tables
-- =====================================

-- Daily study metrics for analytics dashboard
CREATE TABLE daily_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    metric_date DATE NOT NULL,
    total_study_time INT DEFAULT 0, -- in minutes
    sessions_completed INT DEFAULT 0,
    average_performance DECIMAL(3,2),
    subjects_studied JSON,
    goal_achievement_rate DECIMAL(3,2),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_date (user_id, metric_date),
    INDEX idx_metric_date (metric_date)
);

-- =====================================
-- Sample Data for Testing
-- =====================================

-- Insert sample user
INSERT INTO users (username, email, password_hash, first_name, last_name, study_preferences) 
VALUES (
    'demo_user', 
    'demo@studyplanner.com', 
    '$2a$10$DowJonesHash', -- This should be bcrypt hashed password
    'Demo',
    'User',
    '{"preferred_study_time": "morning", "difficulty_preference": "intermediate", "break_frequency": 25}'
);

-- Insert sample study plan
INSERT INTO study_plans (user_id, title, subjects, start_date, end_date, daily_hours, status)
VALUES (
    1,
    'Computer Science Fundamentals',
    '["Data Structures", "Algorithms", "Database Systems", "Software Engineering"]',
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 30 DAY),
    3.0,
    'ACTIVE'
);

-- Insert sample study sessions
INSERT INTO study_sessions (user_id, study_plan_id, subject, topic, duration_minutes, performance_score, completion_rate, difficulty_rating, session_date)
VALUES 
    (1, 1, 'Data Structures', 'Arrays and Lists', 60, 0.85, 0.90, 3, NOW() - INTERVAL 1 DAY),
    (1, 1, 'Algorithms', 'Sorting Algorithms', 90, 0.75, 0.80, 4, NOW() - INTERVAL 1 DAY),
    (1, 1, 'Database Systems', 'SQL Basics', 45, 0.95, 1.00, 2, NOW());

-- =====================================
-- Views for Common Queries
-- =====================================

-- User study progress overview
CREATE VIEW user_study_overview AS
SELECT 
    u.id as user_id,
    u.username,
    COUNT(DISTINCT sp.id) as active_plans,
    COUNT(ss.id) as total_sessions,
    SUM(ss.duration_minutes) as total_study_minutes,
    AVG(ss.performance_score) as avg_performance,
    MAX(ss.session_date) as last_study_session
FROM users u
LEFT JOIN study_plans sp ON u.id = sp.user_id AND sp.status = 'ACTIVE'
LEFT JOIN study_sessions ss ON u.id = ss.user_id
GROUP BY u.id, u.username;

-- Subject performance analysis
CREATE VIEW subject_performance AS
SELECT 
    user_id,
    subject,
    COUNT(*) as session_count,
    SUM(duration_minutes) as total_minutes,
    AVG(performance_score) as avg_performance,
    AVG(completion_rate) as avg_completion,
    AVG(difficulty_rating) as avg_difficulty,
    MAX(session_date) as last_session
FROM study_sessions
GROUP BY user_id, subject
ORDER BY user_id, avg_performance ASC;

-- =====================================
-- Stored Procedures
-- =====================================

DELIMITER //

-- Get user recommendations
CREATE PROCEDURE GetUserRecommendations(IN p_user_id BIGINT)
BEGIN
    SELECT 
        r.*,
        sp.title as study_plan_title
    FROM recommendations r
    LEFT JOIN study_plans sp ON r.study_plan_id = sp.id
    WHERE r.user_id = p_user_id 
    AND r.status = 'PENDING'
    AND r.expires_date > NOW()
    ORDER BY r.confidence_score DESC, r.created_date DESC;
END //

-- Calculate daily metrics
CREATE PROCEDURE CalculateDailyMetrics(IN p_user_id BIGINT, IN p_date DATE)
BEGIN
    INSERT INTO daily_metrics (user_id, metric_date, total_study_time, sessions_completed, average_performance, subjects_studied)
    SELECT 
        p_user_id,
        p_date,
        COALESCE(SUM(duration_minutes), 0),
        COUNT(*),
        AVG(performance_score),
        JSON_ARRAYAGG(DISTINCT subject)
    FROM study_sessions
    WHERE user_id = p_user_id 
    AND DATE(session_date) = p_date
    ON DUPLICATE KEY UPDATE
        total_study_time = VALUES(total_study_time),
        sessions_completed = VALUES(sessions_completed),
        average_performance = VALUES(average_performance),
        subjects_studied = VALUES(subjects_studied);
END //

DELIMITER ;

-- =====================================
-- Indexes for Performance Optimization
-- =====================================

-- Composite indexes for common query patterns
CREATE INDEX idx_sessions_user_date ON study_sessions(user_id, session_date DESC);
CREATE INDEX idx_recommendations_user_status ON recommendations(user_id, status, created_date DESC);
CREATE INDEX idx_plans_user_status ON study_plans(user_id, status);

-- Full-text search indexes (if needed)
-- CREATE FULLTEXT INDEX ft_study_plans_title_desc ON study_plans(title, description);

COMMIT;