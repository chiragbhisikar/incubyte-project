package com.incubyte.incubyte_project_backend.security.auth;

import com.incubyte.incubyte_project_backend.entity.User;
import com.incubyte.incubyte_project_backend.exception.UserNotFoundException;
import com.incubyte.incubyte_project_backend.repository.user.UserRepository;
import com.incubyte.incubyte_project_backend.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {
    private final UserRepository userRepository;

    public Authentication getAuthentication(String username) {
        // Load the user from DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        SecurityUser securityUser = new SecurityUser(user);

        return new UsernamePasswordAuthenticationToken(user, null, securityUser.getAuthorities());
    }
}
