package com.incubyte.incubyte_project_backend.security.contract;

import com.incubyte.incubyte_project_backend.entity.User;

public interface ITokenProvider {
    String generateToken(User user);
    String extractUsername(String token);
}
