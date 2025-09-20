package com.incubyte.incubyte_project_backend.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing permission types in the system.
 * 
 * This enum defines granular permissions that can be assigned to roles.
 * Permissions follow a resource:action format and are used for fine-grained
 * access control. Each permission has a string representation that can be
 * used in security expressions and authorization checks.
 * 
 * Note: This enum contains generic permissions that can be extended
 * for different business domains (patient management, appointments, etc.).
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Getter
@RequiredArgsConstructor
public enum PermissionType {
    
    /**
     * Permission to read patient information.
     * String representation: "patient:read"
     */
    PATIENT_READ("patient:read"),
    
    /**
     * Permission to create and update patient information.
     * String representation: "patient:write"
     */
    PATIENT_WRITE("patient:write"),
    
    /**
     * Permission to read appointment information.
     * String representation: "appointment:read"
     */
    APPOINTMENT_READ("appointment:read"),
    
    /**
     * Permission to create and update appointments.
     * String representation: "appointment:write"
     */
    APPOINTMENT_WRITE("appointment:write"),
    
    /**
     * Permission to delete appointments.
     * String representation: "appointment:delete"
     */
    APPOINTMENT_DELETE("appointment:delete"),
    
    /**
     * Permission to manage user accounts and system administration.
     * String representation: "user:manage"
     * This is typically assigned to admin roles.
     */
    USER_MANAGE("user:manage"),
    
    /**
     * Permission to view system reports and analytics.
     * String representation: "report:view"
     */
    REPORT_VIEW("report:view");

    /**
     * String representation of the permission.
     * Used in security expressions and authorization checks.
     */
    private final String permission;
}
