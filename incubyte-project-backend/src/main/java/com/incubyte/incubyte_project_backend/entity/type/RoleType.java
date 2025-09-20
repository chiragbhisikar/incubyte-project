package com.incubyte.incubyte_project_backend.entity.type;

/**
 * Enumeration representing user roles in the sweet shop management system.
 * 
 * This enum defines the different types of users and their access levels
 * within the application. Roles are used for role-based access control (RBAC)
 * to determine what operations a user can perform.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
public enum RoleType {
    
    /**
     * Administrator role with full access to all system features.
     * Admins can:
     * - Manage sweet inventory (add, update, delete sweets)
     * - Restock inventory
     * - View all system data
     * - Manage user accounts
     */
    ADMIN,
    
    /**
     * Regular user role with limited access to system features.
     * Users can:
     * - View available sweets
     * - Search and browse sweet catalog
     * - Purchase sweets
     * - View their own profile
     */
    USER
}
