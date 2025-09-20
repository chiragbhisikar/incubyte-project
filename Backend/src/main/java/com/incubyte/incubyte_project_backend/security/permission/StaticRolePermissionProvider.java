package com.incubyte.incubyte_project_backend.security.permission;

import com.incubyte.incubyte_project_backend.entity.type.PermissionType;
import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.incubyte.incubyte_project_backend.entity.type.PermissionType.*;
import static com.incubyte.incubyte_project_backend.entity.type.RoleType.*;

@Component
public class StaticRolePermissionProvider implements RolePermissionProvider {

    private static final Map<RoleType, Set<PermissionType>> ROLE_PERMISSIONS = Map.of(
            ADMIN, Set.of(PATIENT_READ, PATIENT_WRITE, APPOINTMENT_READ, APPOINTMENT_WRITE, APPOINTMENT_DELETE, USER_MANAGE, REPORT_VIEW)
            // Extend easily with DOCTOR, PATIENT etc.
    );

    @Override
    public Set<PermissionType> getPermissions(RoleType role) {
        return ROLE_PERMISSIONS.getOrDefault(role, Set.of());
    }
}
