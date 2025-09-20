package com.incubyte.incubyte_project_backend.security.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthorityMapper {
    Collection<? extends GrantedAuthority> mapAuthoritiesFromDomainRoles(Collection<?> roles);
}
