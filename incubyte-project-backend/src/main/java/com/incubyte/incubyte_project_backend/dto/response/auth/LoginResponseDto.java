package com.incubyte.incubyte_project_backend.dto.response.auth;

import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object for successful login responses.
 * 
 * This DTO encapsulates the data returned after a successful user authentication.
 * It contains the JWT token for subsequent API calls and the user's unique identifier.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    
    /**
     * JSON Web Token (JWT) for authenticated API access.
     * This token should be included in the Authorization header of subsequent requests
     * in the format: "Bearer {jwt}"
     * 
     * The token contains user information and permissions and is used for
     * stateless authentication across the application.
     */
    String jwt;
    
    /**
     * Unique identifier of the authenticated user.
     * This UUID can be used to identify the user in subsequent operations
     * and for audit logging purposes.
     */
    UUID userId;


    /*
     * Set of roles assigned to the user.
     */
    Set<RoleType> roles;
}
