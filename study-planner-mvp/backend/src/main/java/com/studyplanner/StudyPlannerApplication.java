package com.studyplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main Spring Boot Application Class for AI-Backed Study Planner MVP
 * 
 * @author Garv Rahut & Shashwat Parashar
 * @version 1.0.0-MVP
 */
@SpringBootApplication
public class StudyPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyPlannerApplication.class, args);
        System.out.println("🚀 AI-Backed Study Planner Backend Started Successfully!");
        System.out.println("📊 API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("🌐 Web Interface: http://localhost:8080/");
    }

    /**
     * Bean for REST template to communicate with ML service
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}