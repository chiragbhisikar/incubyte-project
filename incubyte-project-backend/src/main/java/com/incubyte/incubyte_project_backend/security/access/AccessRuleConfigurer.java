package com.incubyte.incubyte_project_backend.security.access;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface AccessRuleConfigurer {
    void configure(HttpSecurity http) throws Exception;
}
