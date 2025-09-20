package com.incubyte.incubyte_project_backend.security.contract;

import com.incubyte.incubyte_project_backend.dto.request.auth.LoginRequestDto;
import com.incubyte.incubyte_project_backend.dto.response.auth.LoginResponseDto;

public interface IAuthenticationService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
