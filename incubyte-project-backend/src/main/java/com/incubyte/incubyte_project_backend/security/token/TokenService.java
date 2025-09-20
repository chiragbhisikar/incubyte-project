package com.incubyte.incubyte_project_backend.security.token;

import com.incubyte.incubyte_project_backend.entity.User;

public interface TokenService {
    String extractUsername(String token);
    boolean validateToken(String token, User user);
}
