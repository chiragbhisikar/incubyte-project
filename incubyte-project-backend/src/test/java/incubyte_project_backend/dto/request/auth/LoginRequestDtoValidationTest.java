package incubyte_project_backend.dto.request.auth;

import com.incubyte.incubyte_project_backend.dto.request.auth.LoginRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("LoginRequestDto Validation Tests")
class LoginRequestDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation with valid email and password")
    void shouldPassValidation_WithValidEmailAndPassword() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("chiragbhisikar2208@gmail.com", "Chirag#2208");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when email is null")
    void shouldFailValidation_WhenEmailIsNull() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto(null, "Chirag#2208");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when email is blank")
    void shouldFailValidation_WhenEmailIsBlank() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("", "Chirag#2208");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when email format is invalid")
    void shouldFailValidation_WhenEmailFormatIsInvalid() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("invalid-email", "Chirag#2208");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when password is null")
    void shouldFailValidation_WhenPasswordIsNull() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("chiragbhisikar2208@gmail.com", null);

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when password is blank")
    void shouldFailValidation_WhenPasswordIsBlank() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("chiragbhisikar2208@gmail.com", "");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when both email and password are invalid")
    void shouldFailValidation_WhenBothEmailAndPasswordInvalid() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("invalid-email", "");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password is required")));
    }

    @Test
    @DisplayName("Should pass validation with valid email formats")
    void shouldPassValidation_WithValidEmailFormats() {
        // Test cases for valid email formats
        String[] validEmails = {
            "chiragbhisikar2208@gmail.com",
            "user@company.co.uk",
            "user@mail.company.com",
            "user+tag@example.com",
            "user123@example.com",
            "user.name@example.com",
            "user_name@example.com",
            "user-name@example.com"
        };

        for (String email : validEmails) {
            // Arrange
            LoginRequestDto request = new LoginRequestDto(email, "Chirag#2208");

            // Act
            Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty(), "Email " + email + " should be valid");
        }
    }

    @Test
    @DisplayName("Should fail validation with invalid email formats")
    void shouldFailValidation_WithInvalidEmailFormats() {
        // Test cases for invalid email formats
        String[] invalidEmails = {
            "test@",
            "@example.com",
            "test.example.com",
            "test@.com",
            "test@example.",
            "test@@example.com"
        };

        for (String email : invalidEmails) {
            // Arrange
            LoginRequestDto request = new LoginRequestDto(email, "Chirag#2208");

            // Act
            Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

            // Assert
            assertEquals(1, violations.size(), "Email " + email + " should be invalid");
            assertEquals("Invalid email format", violations.iterator().next().getMessage());
        }
    }
}