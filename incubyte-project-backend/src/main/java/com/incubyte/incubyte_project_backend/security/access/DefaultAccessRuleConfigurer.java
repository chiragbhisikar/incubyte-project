package com.incubyte.incubyte_project_backend.security.access;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import static com.incubyte.incubyte_project_backend.entity.type.PermissionType.*;
import static com.incubyte.incubyte_project_backend.entity.type.RoleType.*;

@Component
public class DefaultAccessRuleConfigurer implements AccessRuleConfigurer {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // auth
                        .requestMatchers("/api/sweets", "/api/sweets/{sweetId}", "/api/sweets/search").permitAll()
//                .hasAnyAuthority(APPOINTMENT_DELETE.name(), USER_MANAGE.name())
//                .requestMatchers("/admin/**").hasRole(ADMIN.name())
                        .anyRequest().authenticated()
        );
    }
}
