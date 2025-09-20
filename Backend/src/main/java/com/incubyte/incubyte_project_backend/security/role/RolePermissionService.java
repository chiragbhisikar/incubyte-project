package com.incubyte.incubyte_project_backend.security.role;

import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {
    private final RoleAuthorityProvider roleAuthorityProvider;
    private final AuthorityConverter authorityConverter;

    public RolePermissionService(RoleAuthorityProvider roleAuthorityProvider,
                                 AuthorityConverter authorityConverter) {
        this.roleAuthorityProvider = roleAuthorityProvider;
        this.authorityConverter = authorityConverter;
    }

    public Set<GrantedAuthority> mapRolesToAuthorities(Set<RoleType> roles) {
        Set<String> domainAuthorities = roles.stream()
                .flatMap(role -> roleAuthorityProvider.getAuthorities(role).stream())
                .collect(Collectors.toSet());

        return authorityConverter.convert(domainAuthorities);
    }
}
