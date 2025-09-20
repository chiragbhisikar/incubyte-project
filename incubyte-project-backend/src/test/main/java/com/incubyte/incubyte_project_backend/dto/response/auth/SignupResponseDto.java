package com.incubyte.incubyte_project_backend.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object for successful user registration responses.
 * 
 * This DTO encapsulates the data returned after a successful user registration.
 * It contains the newly created user's unique identifier and username (email)
 * for confirmation purposes.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDto {
    
    /**
     * Unique identifier of the newly created user.
     * This UUID is generated automatically and can be used to identify
     * the user in subsequent operations.
     */
    private UUID id;
    
    /**
     * Username (email address) of the newly created user.
     * This confirms the email address that was used for registration
     * and can be used for login purposes.
     */
    private String username;
}
