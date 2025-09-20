package com.incubyte.incubyte_project_backend.security.service;

import com.incubyte.incubyte_project_backend.dto.request.auth.LoginRequestDto;
import com.incubyte.incubyte_project_backend.dto.response.auth.LoginResponseDto;
import com.incubyte.incubyte_project_backend.entity.User;
import com.incubyte.incubyte_project_backend.security.SecurityUser;
import com.incubyte.incubyte_project_backend.security.contract.IAuthenticationService;
import com.incubyte.incubyte_project_backend.security.contract.ITokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final ITokenProvider tokenProvider; // depends on abstraction

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = securityUser.getUser();
        String token = tokenProvider.generateToken(user);

        return new LoginResponseDto(token, user.getId(), user.getRoles());
    }
}
