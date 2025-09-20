package incubyte_project_backend.dto.request.auth;

import com.incubyte.incubyte_project_backend.dto.request.auth.SignUpRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SignUpRequestDto Validation Tests")
class SignUpRequestDtoValidationTest {

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
        SignUpRequestDto request = new SignUpRequestDto("user@example.com", "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when email is null")
    void shouldFailValidation_WhenEmailIsNull() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto(null, "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when email is blank")
    void shouldFailValidation_WhenEmailIsBlank() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("", "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when email format is invalid")
    void shouldFailValidation_WhenEmailFormatIsInvalid() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("invalid-email", "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when password is null")
    void shouldFailValidation_WhenPasswordIsNull() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("user@example.com", null);

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when password is blank")
    void shouldFailValidation_WhenPasswordIsBlank() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("user@example.com", "");

        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Collect all messages
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        // Assert the number of violations
        assertEquals(2, violations.size());

        // Assert that expected messages exist
        assertTrue(messages.contains("Password is required"));
        assertTrue(messages.contains("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"));
    }

    @Test
    @DisplayName("Should fail validation when password lacks uppercase letter")
    void shouldFailValidation_WhenPasswordLacksUppercase() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("user@example.com", "password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when password lacks lowercase letter")
    void shouldFailValidation_WhenPasswordLacksLowercase() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("user@example.com", "PASSWORD@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when password lacks number")
    void shouldFailValidation_WhenPasswordLacksNumber() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("user@example.com", "Password@");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when password lacks special character")
    void shouldFailValidation_WhenPasswordLacksSpecialChar() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("user@example.com", "Password123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when password is too short")
    void shouldFailValidation_WhenPasswordTooShort() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("user@example.com", "Pass@1");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when both email and password are invalid")
    void shouldFailValidation_WhenBothEmailAndPasswordInvalid() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("invalid-email", "weak");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")));
    }

    @Test
    @DisplayName("Should pass validation with valid email formats")
    void shouldPassValidation_WithValidEmailFormats() {
        // Test cases for valid email formats
        String[] validEmails = {
                "user@example.com",
                "user@company.co.uk",
                "user@mail.company.com",
                "user+tag@example.com",
                "user123@example.com",
                "user.name@example.com",
                "user_name@example.com"
        };

        for (String email : validEmails) {
            // Arrange
            SignUpRequestDto request = new SignUpRequestDto(email, "Password@123");

            // Act
            Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty(), "Email " + email + " should be valid");
        }
    }

    @Test
    @DisplayName("Should pass validation with valid password formats")
    void shouldPassValidation_WithValidPasswordFormats() {
        // Test cases for valid password formats
        String[] validPasswords = {
                "Password@123",
                "Password#123",
                "Password$123",
                "Password!123",
                "Password%123",
                "Password*123",
                "Password?123",
                "Password&123"
        };

        for (String password : validPasswords) {
            // Arrange
            SignUpRequestDto request = new SignUpRequestDto("user@example.com", password);

            // Act
            Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

            // Assert
            assertTrue(violations.isEmpty(), "Password " + password + " should be valid");
        }
    }
}
