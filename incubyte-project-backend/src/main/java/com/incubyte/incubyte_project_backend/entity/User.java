package com.incubyte.incubyte_project_backend.entity;

import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * User entity representing a user in the sweet shop management system.
 * 
 * This entity stores user information including authentication credentials
 * and role-based access control. Users can have multiple roles (USER, ADMIN)
 * and are used for authentication and authorization throughout the application.
 * 
 * Database table: user
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    /**
     * Unique identifier for the user.
     * Generated automatically using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * User's email address used as username for authentication.
     * Must be unique across all users and cannot be null.
     * 
     * Database column: email
     */
    @Column(name = "email", unique = true, nullable = false)
    private String username;

    /**
     * User's password for authentication.
     * Should be encoded using BCrypt before storing in database.
     */
    private String password;

    /**
     * Set of roles assigned to the user.
     * Uses eager fetching to load roles immediately when user is loaded.
     * Roles are stored as strings in a separate table.
     * 
     * Default roles: USER, ADMIN
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles = new HashSet<>();
}
