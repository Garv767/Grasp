package com.studyplanner.controller;

import com.studyplanner.dto.UserRegistrationRequest;
import com.studyplanner.dto.UserLoginRequest;
import com.studyplanner.dto.ApiResponse;
import com.studyplanner.model.User;
import com.studyplanner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User Management REST Controller for AI-Backed Study Planner
 * Handles user registration, authentication, and profile management
 * 
 * @author Garv Rahut & Shashwat Parashar
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "User registration, authentication, and profile APIs")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Primary Endpoint: User Registration
     * Creates a new user account with validation
     */
    @Operation(summary = "Register new user", description = "Create a new user account with username, email, and password")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            User createdUser = userService.registerUser(request);
            
            ApiResponse<User> response = new ApiResponse<>(
                true,
                "User registered successfully",
                createdUser
            );
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (IllegalArgumentException e) {
            ApiResponse<User> response = new ApiResponse<>(
                false,
                "Registration failed: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            
        } catch (Exception e) {
            ApiResponse<User> response = new ApiResponse<>(
                false,
                "Registration failed: Internal server error",
                null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * User Authentication/Login
     * Authenticates user credentials and returns JWT token
     */
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> loginUser(@Valid @RequestBody UserLoginRequest request) {
        try {
            String token = userService.authenticateUser(request.getUsername(), request.getPassword());
            User user = userService.findByUsername(request.getUsername());
            
            // Create response with token and user info
            var loginResponse = new Object() {
                public final Long userId = user.getId();
                public final String token = token;
                public final String username = user.getUsername();
                public final String fullName = user.getFullName();
                public final String message = "Login successful";
            };
            
            ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Login successful",
                loginResponse
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Login failed: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            
        } catch (Exception e) {
            ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Login failed: Internal server error",
                null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Get User Profile
     * Retrieves the authenticated user's profile information
     */
    @Operation(summary = "Get user profile", description = "Retrieve user profile information")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extract user ID from JWT token (simplified for MVP)
            Long userId = userService.getUserIdFromToken(authHeader);
            User user = userService.findById(userId);
            
            ApiResponse<User> response = new ApiResponse<>(
                true,
                "Profile retrieved successfully",
                user
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            ApiResponse<User> response = new ApiResponse<>(
                false,
                "Failed to retrieve profile: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            
        } catch (Exception e) {
            ApiResponse<User> response = new ApiResponse<>(
                false,
                "Failed to retrieve profile: Internal server error",
                null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Update User Profile
     * Updates the authenticated user's profile information
     */
    @Operation(summary = "Update user profile", description = "Update user profile information")
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<User>> updateUserProfile(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UserRegistrationRequest updateRequest) {
        try {
            Long userId = userService.getUserIdFromToken(authHeader);
            User updatedUser = userService.updateProfile(userId, updateRequest);
            
            ApiResponse<User> response = new ApiResponse<>(
                true,
                "Profile updated successfully",
                updatedUser
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            ApiResponse<User> response = new ApiResponse<>(
                false,
                "Failed to update profile: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            
        } catch (Exception e) {
            ApiResponse<User> response = new ApiResponse<>(
                false,
                "Failed to update profile: Internal server error",
                null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Health check endpoint for user service
     */
    @Operation(summary = "User service health check")
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        ApiResponse<String> response = new ApiResponse<>(
            true,
            "User service is running",
            "OK"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}