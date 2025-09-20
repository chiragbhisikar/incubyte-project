package com.incubyte.incubyte_project_backend.security.contract;

import com.incubyte.incubyte_project_backend.dto.request.auth.SignUpRequestDto;
import com.incubyte.incubyte_project_backend.dto.response.auth.SignupResponseDto;
import com.incubyte.incubyte_project_backend.exception.UserAlreadyExistException;

public interface IRegistrationService {
    SignupResponseDto register(SignUpRequestDto signupRequestDto) throws UserAlreadyExistException;
}
