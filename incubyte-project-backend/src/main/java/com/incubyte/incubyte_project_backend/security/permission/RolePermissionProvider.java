package com.incubyte.incubyte_project_backend.security.permission;

import com.incubyte.incubyte_project_backend.entity.type.PermissionType;
import com.incubyte.incubyte_project_backend.entity.type.RoleType;

import java.util.Set;

public interface RolePermissionProvider {
    Set<PermissionType> getPermissions(RoleType role);
}
