-- Grasp Database Schema (PostgreSQL)
-- AI-Backed Study Planner and Progress Tracker

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Study Plans table
CREATE TABLE IF NOT EXISTS study_plans (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    deadline DATE,
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
-- enforce allowed values for status
ALTER TABLE study_plans
  ADD CONSTRAINT chk_study_plans_status CHECK (status IN ('ACTIVE','COMPLETED','PAUSED'));

-- Subjects table
CREATE TABLE IF NOT EXISTS subjects (
    id BIGSERIAL PRIMARY KEY,
    plan_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    difficulty_level VARCHAR(16) NOT NULL DEFAULT 'BEGINNER',
    time_allocation_minutes INT NOT NULL DEFAULT 0,
    current_progress_percent NUMERIC(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES study_plans(id) ON DELETE CASCADE
);
-- enforce allowed values for difficulty_level
ALTER TABLE subjects
  ADD CONSTRAINT chk_subjects_difficulty CHECK (difficulty_level IN ('BEGINNER','INTERMEDIATE','ADVANCED'));

-- Study Sessions table
CREATE TABLE IF NOT EXISTS study_sessions (
    id BIGSERIAL PRIMARY KEY,
    plan_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    duration_minutes INT NOT NULL,
    completion_percent NUMERIC(5,2) NOT NULL,
    performance_score INT CHECK (performance_score >= 1 AND performance_score <= 10),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES study_plans(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- Recommendations table
CREATE TABLE IF NOT EXISTS recommendations (
    id BIGSERIAL PRIMARY KEY,
    plan_id BIGINT NOT NULL,
    subject_id BIGINT,
    type VARCHAR(32) NOT NULL,
    value VARCHAR(500) NOT NULL,
    confidence_score NUMERIC(3,2) CHECK (confidence_score >= 0.00 AND confidence_score <= 1.00),
    reasoning TEXT,
    status VARCHAR(16) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES study_plans(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE SET NULL
);
-- enforce allowed values for recommendation type and status
ALTER TABLE recommendations
  ADD CONSTRAINT chk_recommendations_type CHECK (type IN ('TIME_ADJUSTMENT','DIFFICULTY_CHANGE','SCHEDULE_MODIFICATION')),
  ADD CONSTRAINT chk_recommendations_status CHECK (status IN ('PENDING','APPLIED','DISMISSED'));

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_study_plans_user_id ON study_plans(user_id);
CREATE INDEX IF NOT EXISTS idx_study_plans_status ON study_plans(status);
CREATE INDEX IF NOT EXISTS idx_subjects_plan_id ON subjects(plan_id);
CREATE INDEX IF NOT EXISTS idx_study_sessions_plan_id ON study_sessions(plan_id);
CREATE INDEX IF NOT EXISTS idx_study_sessions_subject_id ON study_sessions(subject_id);
CREATE INDEX IF NOT EXISTS idx_study_sessions_created_at ON study_sessions(created_at);
CREATE INDEX IF NOT EXISTS idx_recommendations_plan_id ON recommendations(plan_id);
CREATE INDEX IF NOT EXISTS idx_recommendations_status ON recommendations(status);
