package com.incubyte.incubyte_project_backend.security.permission;

import com.incubyte.incubyte_project_backend.entity.type.PermissionType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionToAuthorityConverter {
    public Set<GrantedAuthority> convert(Set<PermissionType> permissions) {
//        Set<PermissionType> permissions = rolePermissionProvider.getPermissions(role);
//        Set<GrantedAuthority> authorities = converter.convert(permissions);
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
