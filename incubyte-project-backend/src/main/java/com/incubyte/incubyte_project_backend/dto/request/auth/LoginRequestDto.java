package com.incubyte.incubyte_project_backend.dto.request.auth;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for user login requests.
 * 
 * This DTO encapsulates the data required for user authentication.
 * It contains the user's email (used as username) and password for
 * login validation. All fields are validated using Bean Validation
 * annotations to ensure data integrity.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    
    /**
     * User's email address used as username for authentication.
     * 
     * Validation rules:
     * - Cannot be null, blank, or whitespace only
     * - Must be a valid email format
     * 
     * @see jakarta.validation.constraints.NotBlank
     * @see jakarta.validation.constraints.Email
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String username;

    /**
     * User's password for authentication.
     * 
     * Validation rules:
     * - Cannot be null, blank, or whitespace only
     * 
     * @see jakarta.validation.constraints.NotBlank
     */
    @NotBlank(message = "Password is required")
    private String password;
}