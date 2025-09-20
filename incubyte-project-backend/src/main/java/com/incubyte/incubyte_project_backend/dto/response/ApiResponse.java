package com.incubyte.incubyte_project_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for all REST API endpoints.
 * 
 * This class provides a consistent response structure across all API endpoints.
 * It wraps the actual response data with a message field for additional context
 * and status information. This ensures uniform response format throughout the application.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    
    /**
     * Human-readable message describing the result of the operation.
     * This field provides context about the success or failure of the request.
     * 
     * Examples:
     * - "Login successful"
     * - "User Registered Successfully !"
     * - "Sweet Added Successfully"
     * - "Sweet not found with id: {id}"
     */
    private String message;
    
    /**
     * The actual response data payload.
     * This can be any object type depending on the endpoint:
     * - LoginResponseDto for authentication
     * - SignupResponseDto for registration
     * - Sweet entity for sweet operations
     * - List of sweets for catalog operations
     * - null for error responses
     */
    private Object data;
}