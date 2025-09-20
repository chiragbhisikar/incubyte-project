package com.incubyte.incubyte_project_backend.security.role;

import com.incubyte.incubyte_project_backend.entity.type.RoleType;

import java.util.Set;

public interface RoleAuthorityProvider {
    Set<String> getAuthorities(RoleType role);
}