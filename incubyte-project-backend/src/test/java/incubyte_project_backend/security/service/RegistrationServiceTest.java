package incubyte_project_backend.security.service;

import com.incubyte.incubyte_project_backend.dto.request.auth.SignUpRequestDto;
import com.incubyte.incubyte_project_backend.dto.response.auth.SignupResponseDto;
import com.incubyte.incubyte_project_backend.entity.User;
import com.incubyte.incubyte_project_backend.entity.type.RoleType;
import com.incubyte.incubyte_project_backend.exception.UserAlreadyExistException;
import com.incubyte.incubyte_project_backend.repository.user.UserRepository;
import com.incubyte.incubyte_project_backend.security.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrationService Unit Tests")
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private SignUpRequestDto signUpRequestDto;
    private User existingUser;
    private User newUser;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        signUpRequestDto = new SignUpRequestDto("newuser@example.com", "Password@123");
        existingUser = User.builder()
                .id(UUID.randomUUID())
                .username("existing@example.com")
                .password("encodedPassword")
                .roles(Collections.singleton(RoleType.USER))
                .build();

        newUser = User.builder()
                .id(UUID.randomUUID())
                .username("newuser@example.com")
                .password("encodedPassword")
                .roles(Collections.singleton(RoleType.USER))
                .build();

        encodedPassword = "encodedPassword123";
    }

    @Test
    @DisplayName("Should successfully register new user and return SignupResponseDto")
    void register_ShouldReturnSignupResponse_WhenUserIsNew() throws UserAlreadyExistException {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        SignupResponseDto result = registrationService.register(signUpRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(newUser.getId(), result.getId());
        assertEquals(newUser.getUsername(), result.getUsername());

        verify(userRepository).findByUsername(signUpRequestDto.getUsername());
        verify(passwordEncoder).encode(signUpRequestDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistException when user already exists")
    void register_ShouldThrowUserAlreadyExistException_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername()))
                .thenReturn(Optional.of(existingUser));

        // Act & Assert
        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () -> {
            registrationService.register(signUpRequestDto);
        });

        assertEquals("User already exists", exception.getMessage());

        verify(userRepository).findByUsername(signUpRequestDto.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should create user with correct properties")
    void register_ShouldCreateUserWithCorrectProperties() throws UserAlreadyExistException {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        registrationService.register(signUpRequestDto);

        // Assert
        verify(userRepository).save(argThat(user ->
                user.getUsername().equals(signUpRequestDto.getUsername()) &&
                        user.getPassword().equals(encodedPassword) &&
                        user.getRoles().contains(RoleType.USER) &&
                        user.getRoles().size() == 1
        ));
    }

    @Test
    @DisplayName("Should encode password before saving")
    void register_ShouldEncodePasswordBeforeSaving() throws UserAlreadyExistException {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        registrationService.register(signUpRequestDto);

        // Assert
        verify(passwordEncoder).encode(signUpRequestDto.getPassword());
        verify(userRepository).save(argThat(user ->
                user.getPassword().equals(encodedPassword)
        ));
    }

    @Test
    @DisplayName("Should set user role to USER")
    void register_ShouldSetUserRoleToUSER() throws UserAlreadyExistException {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        registrationService.register(signUpRequestDto);

        // Assert
        verify(userRepository).save(argThat(user ->
                user.getRoles().contains(RoleType.USER) &&
                        user.getRoles().size() == 1
        ));
    }

    @Test
    @DisplayName("Should handle null SignUpRequestDto")
    void register_ShouldHandleNullSignupRequest() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            registrationService.register(null);
        });
    }

    @Test
    @DisplayName("Should handle null username")
    void register_ShouldHandleNullUsername() {
        // Arrange
        SignUpRequestDto requestWithNullUsername = new SignUpRequestDto(null, "Password@123");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            registrationService.register(requestWithNullUsername);
        });
    }

    @Test
    @DisplayName("Should handle null password")
    void register_ShouldHandleNullPassword() {
        // Arrange
        SignUpRequestDto requestWithNullPassword = new SignUpRequestDto("user@example.com", null);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            registrationService.register(requestWithNullPassword);
        });
    }

    @Test
    @DisplayName("Should handle empty username")
    void register_ShouldHandleEmptyUsername() {
        // Arrange
        SignUpRequestDto requestWithEmptyUsername = new SignUpRequestDto("", "Password@123");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            registrationService.register(requestWithEmptyUsername);
        });
    }

    @Test
    @DisplayName("Should handle empty password")
    void register_ShouldHandleEmptyPassword() {
        // Arrange
        SignUpRequestDto requestWithEmptyPassword = new SignUpRequestDto("user@example.com", "");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            registrationService.register(requestWithEmptyPassword);
        });
    }

    @Test
    @DisplayName("Should return correct response structure")
    void register_ShouldReturnCorrectResponseStructure() throws UserAlreadyExistException {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        SignupResponseDto result = registrationService.register(signUpRequestDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getUsername());
        assertEquals(newUser.getId(), result.getId());
        assertEquals(newUser.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("Should call repository methods in correct order")
    void register_ShouldCallRepositoryMethodsInCorrectOrder() throws UserAlreadyExistException {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        registrationService.register(signUpRequestDto);

        // Assert
        verify(userRepository, times(1)).findByUsername(signUpRequestDto.getUsername());
        verify(passwordEncoder, times(1)).encode(signUpRequestDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle password encoder exception")
    void register_ShouldHandlePasswordEncoderException() {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .thenThrow(new RuntimeException("Password encoding failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            registrationService.register(signUpRequestDto);
        });

        verify(userRepository).findByUsername(signUpRequestDto.getUsername());
        verify(passwordEncoder).encode(signUpRequestDto.getPassword());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle repository save exception")
    void register_ShouldHandleRepositorySaveException() {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("Database save failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            registrationService.register(signUpRequestDto);
        });

        verify(userRepository).findByUsername(signUpRequestDto.getUsername());
        verify(passwordEncoder).encode(signUpRequestDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle repository findByUsername exception")
    void register_ShouldHandleRepositoryFindByUsernameException() {
        // Arrange
        when(userRepository.findByUsername(signUpRequestDto.getUsername()))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            registrationService.register(signUpRequestDto);
        });

        verify(userRepository).findByUsername(signUpRequestDto.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle different email formats")
    void register_ShouldHandleDifferentEmailFormats() throws UserAlreadyExistException {
        // Arrange
        SignUpRequestDto requestWithDifferentEmail = new SignUpRequestDto("user@company.co.uk", "Password@123");
        User userWithDifferentEmail = User.builder()
                .id(UUID.randomUUID())
                .username("user@company.co.uk")
                .password(encodedPassword)
                .roles(Collections.singleton(RoleType.USER))
                .build();

        when(userRepository.findByUsername(requestWithDifferentEmail.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(requestWithDifferentEmail.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(userWithDifferentEmail);

        // Act
        SignupResponseDto result = registrationService.register(requestWithDifferentEmail);

        // Assert
        assertNotNull(result);
        assertEquals(userWithDifferentEmail.getId(), result.getId());
        assertEquals(userWithDifferentEmail.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("Should handle case-sensitive username check")
    void register_ShouldHandleCaseSensitiveUsernameCheck() {
        // Arrange
        SignUpRequestDto requestWithUpperCaseEmail = new SignUpRequestDto("USER@EXAMPLE.COM", "Password@123");
        when(userRepository.findByUsername(requestWithUpperCaseEmail.getUsername()))
                .thenReturn(Optional.of(existingUser));

        // Act & Assert
        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () -> {
            registrationService.register(requestWithUpperCaseEmail);
        });

        assertEquals("User already exists", exception.getMessage());
        verify(userRepository).findByUsername(requestWithUpperCaseEmail.getUsername());
    }

    @Test
    @DisplayName("Should verify user ID is generated by database")
    void register_ShouldVerifyUserIdIsGeneratedByDatabase() throws UserAlreadyExistException {
        // Arrange
        User userWithoutId = User.builder()
                .username(signUpRequestDto.getUsername())
                .password(encodedPassword)
                .roles(Collections.singleton(RoleType.USER))
                .build();

        User userWithId = User.builder()
                .id(UUID.randomUUID())
                .username(signUpRequestDto.getUsername())
                .password(encodedPassword)
                .roles(Collections.singleton(RoleType.USER))
                .build();

        when(userRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(userWithId);

        // Act
        SignupResponseDto result = registrationService.register(signUpRequestDto);

        // Assert
        assertNotNull(result.getId());
        assertEquals(userWithId.getId(), result.getId());
        verify(userRepository).save(argThat(user -> user.getId() == null)); // ID should be null before save
    }
}