package com.incubyte.incubyte_project_backend.security.token;

import com.incubyte.incubyte_project_backend.entity.User;
import com.incubyte.incubyte_project_backend.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String extractUsername(String token) {
        return jwtTokenProvider.extractUsername(token);
    }

    @Override
    public boolean validateToken(String token, User user) {
        return user.getUsername().equals(extractUsername(token));
    }
}
