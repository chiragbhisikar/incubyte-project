package incubyte_project_backend.security.service;

import com.incubyte.incubyte_project_backend.dto.request.auth.LoginRequestDto;
import com.incubyte.incubyte_project_backend.dto.response.auth.LoginResponseDto;
import com.incubyte.incubyte_project_backend.entity.User;
import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import com.incubyte.incubyte_project_backend.security.SecurityUser;
import com.incubyte.incubyte_project_backend.security.contract.ITokenProvider;
import com.incubyte.incubyte_project_backend.security.service.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService Unit Tests")
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ITokenProvider tokenProvider;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User testUser;
    private SecurityUser securityUser;
    private LoginRequestDto loginRequestDto;
    private String testToken;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .username("test@example.com")
                .password("encodedPassword")
                .roles(Collections.singleton(RoleType.USER))
                .build();

        securityUser = new SecurityUser(testUser);
        loginRequestDto = new LoginRequestDto("test@example.com", "Password@123");
        testToken = "jwt-token-123";
    }

    @Test
    @DisplayName("Should return LoginResponseDto when authentication is successful")
    void login_ShouldReturnLoginResponse_WhenAuthenticationSuccessful() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser)).thenReturn(testToken);

        // Act
        LoginResponseDto result = authenticationService.login(loginRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(testToken, result.getJwt());
        assertEquals(testUser.getId(), result.getUserId());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(testUser);
    }

    @Test
    @DisplayName("Should call AuthenticationManager with correct credentials")
    void login_ShouldCallAuthenticationManagerWithCorrectCredentials() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser)).thenReturn(testToken);

        // Act
        authenticationService.login(loginRequestDto);

        // Assert
        verify(authenticationManager).authenticate(argThat(token ->
                token instanceof UsernamePasswordAuthenticationToken &&
                loginRequestDto.getUsername().equals(token.getName()) &&
                loginRequestDto.getPassword().equals(token.getCredentials().toString())
        ));
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when authentication fails")
    void login_ShouldThrowBadCredentialsException_WhenAuthenticationFails() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(loginRequestDto);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should extract user from SecurityUser correctly")
    void login_ShouldExtractUserFromSecurityUser() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser)).thenReturn(testToken);

        // Act
        LoginResponseDto result = authenticationService.login(loginRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUserId());
        verify(tokenProvider).generateToken(testUser);
    }

    @Test
    @DisplayName("Should handle null LoginRequestDto")
    void login_ShouldHandleNullLoginRequest() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            authenticationService.login(null);
        });
    }

    @Test
    @DisplayName("Should handle null username")
    void login_ShouldHandleNullUsername() {
        // Arrange
        LoginRequestDto requestWithNullUsername = new LoginRequestDto(null, "Password@123");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            authenticationService.login(requestWithNullUsername);
        });
    }

    @Test
    @DisplayName("Should handle null password")
    void login_ShouldHandleNullPassword() {
        // Arrange
        LoginRequestDto requestWithNullPassword = new LoginRequestDto("test@example.com", null);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            authenticationService.login(requestWithNullPassword);
        });
    }

    @Test
    @DisplayName("Should handle empty credentials")
    void login_ShouldHandleEmptyCredentials() {
        // Arrange
        LoginRequestDto requestWithEmptyCredentials = new LoginRequestDto("", "");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            authenticationService.login(requestWithEmptyCredentials);
        });
    }

    @Test
    @DisplayName("Should generate token with correct user")
    void login_ShouldGenerateTokenWithCorrectUser() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser)).thenReturn(testToken);

        // Act
        authenticationService.login(loginRequestDto);

        // Assert
        verify(tokenProvider).generateToken(argThat(user ->
                user.getId().equals(testUser.getId()) &&
                user.getUsername().equals(testUser.getUsername()) &&
                user.getRoles().equals(testUser.getRoles())
        ));
    }

    @Test
    @DisplayName("Should return correct response structure")
    void login_ShouldReturnCorrectResponseStructure() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser)).thenReturn(testToken);

        // Act
        LoginResponseDto result = authenticationService.login(loginRequestDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getJwt());
        assertNotNull(result.getUserId());
        assertEquals(testToken, result.getJwt());
        assertEquals(testUser.getId(), result.getUserId());
    }

    @Test
    @DisplayName("Should handle different email formats")
    void login_ShouldHandleDifferentEmailFormats() {
        // Arrange
        LoginRequestDto requestWithDifferentEmail = new LoginRequestDto("user@company.co.uk", "Password@123");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser)).thenReturn(testToken);

        // Act
        LoginResponseDto result = authenticationService.login(requestWithDifferentEmail);

        // Assert
        assertNotNull(result);
        assertEquals(testToken, result.getJwt());
        assertEquals(testUser.getId(), result.getUserId());
    }

    @Test
    @DisplayName("Should handle case-sensitive username")
    void login_ShouldHandleCaseSensitiveUsername() {
        // Arrange
        LoginRequestDto requestWithUpperCaseEmail = new LoginRequestDto("TEST@EXAMPLE.COM", "Password@123");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser)).thenReturn(testToken);

        // Act
        LoginResponseDto result = authenticationService.login(requestWithUpperCaseEmail);

        // Assert
        assertNotNull(result);
        assertEquals(testToken, result.getJwt());
        assertEquals(testUser.getId(), result.getUserId());
    }

    @Test
    @DisplayName("Should handle token provider exception")
    void login_ShouldHandleTokenProviderException() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser))
                .thenThrow(new RuntimeException("Token generation failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authenticationService.login(loginRequestDto);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(testUser);
    }

    @Test
    @DisplayName("Should handle authentication manager exception")
    void login_ShouldHandleAuthenticationManagerException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication manager failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authenticationService.login(loginRequestDto);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should handle SecurityUser cast exception")
    void login_ShouldHandleSecurityUserCastException() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("invalid-principal");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Act & Assert
        assertThrows(ClassCastException.class, () -> {
            authenticationService.login(loginRequestDto);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should handle null authentication principal")
    void login_ShouldHandleNullAuthenticationPrincipal() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            authenticationService.login(loginRequestDto);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should verify authentication flow order")
    void login_ShouldVerifyAuthenticationFlowOrder() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(testUser)).thenReturn(testToken);

        // Act
        authenticationService.login(loginRequestDto);

        // Assert
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, times(1)).generateToken(testUser);
    }

    @Test
    @DisplayName("Should handle user with multiple roles")
    void login_ShouldHandleUserWithMultipleRoles() {
        // Arrange
        User userWithMultipleRoles = User.builder()
                .id(UUID.randomUUID())
                .username("admin@example.com")
                .password("encodedPassword")
                .roles(Set.of(RoleType.USER, RoleType.ADMIN))
                .build();

        SecurityUser securityUserWithMultipleRoles = new SecurityUser(userWithMultipleRoles);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUserWithMultipleRoles);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(userWithMultipleRoles)).thenReturn(testToken);

        LoginRequestDto adminLoginRequest = new LoginRequestDto("admin@example.com", "Password@123");

        // Act
        LoginResponseDto result = authenticationService.login(adminLoginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testToken, result.getJwt());
        assertEquals(userWithMultipleRoles.getId(), result.getUserId());
        verify(tokenProvider).generateToken(userWithMultipleRoles);
    }
}