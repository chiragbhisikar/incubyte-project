package com.incubyte.incubyte_project_backend.security.role;

import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SimpleRoleAuthorityProvider implements RoleAuthorityProvider {

    @Override
    public Set<String> getAuthorities(RoleType role) {
        // purely domain-level representation
        return Set.of("ROLE_" + role.name());
    }
}
