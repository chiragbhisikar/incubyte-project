package com.incubyte.incubyte_project_backend.controller;

import com.incubyte.incubyte_project_backend.dto.request.auth.LoginRequestDto;
import com.incubyte.incubyte_project_backend.dto.request.auth.SignUpRequestDto;
import com.incubyte.incubyte_project_backend.dto.response.ApiResponse;
import com.incubyte.incubyte_project_backend.exception.UserAlreadyExistException;
import com.incubyte.incubyte_project_backend.security.contract.IAuthenticationService;
import com.incubyte.incubyte_project_backend.security.contract.IRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling authentication and user registration operations.
 * 
 * This controller provides endpoints for user authentication (login) and registration.
 * It handles JWT-based authentication and user management operations. All endpoints
 * are publicly accessible and do not require authentication.
 * 
 * Base URL: /api/auth
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:3000")
public class AuthController {
    
    /**
     * Service for handling user authentication operations.
     */
    private final IAuthenticationService authenticationService;
    
    /**
     * Service for handling user registration operations.
     */
    private final IRegistrationService registrationService;

    /**
     * Authenticates a user and returns a JWT token.
     * 
     * This endpoint validates user credentials and returns a JWT token
     * for authenticated API access. The request body is validated using
     * Bean Validation annotations.
     * 
     * @param loginRequestDto The login request containing email and password
     * @return ResponseEntity containing the JWT token and user ID
     * 
     * HTTP Status Codes:
     * - 200 OK: Successful authentication
     * - 400 Bad Request: Validation errors
     * - 401 Unauthorized: Invalid credentials
     * 
     * @see LoginRequestDto
     * @see com.incubyte.incubyte_project_backend.dto.response.auth.LoginResponseDto
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Login successful", authenticationService.login(loginRequestDto)));
    }

    /**
     * Registers a new user in the system.
     * 
     * This endpoint creates a new user account with the provided credentials.
     * The password is automatically encoded before storage. The request body
     * is validated using Bean Validation annotations.
     * 
     * @param signupRequestDto The registration request containing email and password
     * @return ResponseEntity containing the created user information
     * 
     * HTTP Status Codes:
     * - 201 Created: Successful registration
     * - 208 Already Reported: User already exists
     * - 400 Bad Request: Validation errors
     * 
     * @see SignUpRequestDto
     * @see com.incubyte.incubyte_project_backend.dto.response.auth.SignupResponseDto
     * @see UserAlreadyExistException
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignUpRequestDto signupRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("User Registered Successfully !", registrationService.register(signupRequestDto)));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body(new ApiResponse("User Already Exist !", null));
        }
    }
}