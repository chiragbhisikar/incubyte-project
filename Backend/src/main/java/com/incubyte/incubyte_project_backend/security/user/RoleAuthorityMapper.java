package com.incubyte.incubyte_project_backend.security.user;

import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleAuthorityMapper implements AuthorityMapper {

    @Override
    public Collection<? extends GrantedAuthority> mapAuthoritiesFromDomainRoles(Collection<?> roles) {
        return ((Set<RoleType>) roles).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}
