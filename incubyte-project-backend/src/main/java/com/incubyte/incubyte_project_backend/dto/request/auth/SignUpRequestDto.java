package com.incubyte.incubyte_project_backend.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user registration requests.
 * 
 * This DTO encapsulates the data required for user registration.
 * It contains the user's email (used as username) and password with
 * strict validation rules to ensure strong password requirements.
 * All fields are validated using Bean Validation annotations.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    
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
     * User's password for authentication with strong security requirements.
     * 
     * Validation rules:
     * - Cannot be null, blank, or whitespace only
     * - Must be at least 8 characters long
     * - Must contain at least one uppercase letter (A-Z)
     * - Must contain at least one lowercase letter (a-z)
     * - Must contain at least one digit (0-9)
     * - Must contain at least one special character (#@$!%*?&)
     * 
     * @see jakarta.validation.constraints.NotBlank
     * @see jakarta.validation.constraints.Pattern
     */
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#@$!%*?&]).{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;
}
