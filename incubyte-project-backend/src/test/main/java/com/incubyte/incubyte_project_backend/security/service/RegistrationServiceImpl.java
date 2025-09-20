package com.incubyte.incubyte_project_backend.security.service;

import com.incubyte.incubyte_project_backend.dto.request.auth.SignUpRequestDto;
import com.incubyte.incubyte_project_backend.dto.response.auth.SignupResponseDto;
import com.incubyte.incubyte_project_backend.entity.User;
import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import com.incubyte.incubyte_project_backend.exception.UserAlreadyExistException;
import com.incubyte.incubyte_project_backend.repository.user.UserRepository;
import com.incubyte.incubyte_project_backend.security.contract.IRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements IRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignupResponseDto register(SignUpRequestDto signupRequestDto) throws UserAlreadyExistException {
        userRepository.findByUsername(signupRequestDto.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException("User already exists");
                });

        User newUser = User.builder()
                .username(signupRequestDto.getUsername())
                .roles(Collections.singleton(RoleType.USER))
                .build();

        newUser.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        User savedUser = userRepository.save(newUser);

        return new SignupResponseDto(savedUser.getId(), savedUser.getUsername());
    }
}
