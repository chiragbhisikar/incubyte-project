package incubyte_project_backend.dto.request.auth;

import com.incubyte.incubyte_project_backend.dto.request.auth.LoginRequestDto;
import com.incubyte.incubyte_project_backend.dto.request.auth.SignUpRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Auth Request DTO Validation Tests")
class AuthRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ========== LOGIN REQUEST DTO VALIDATION TESTS ==========

    @Test
    @DisplayName("LoginRequestDto - Should pass validation with valid data")
    void loginRequestDto_ShouldPassValidation_WithValidData() {
        // Arrange
        LoginRequestDto validRequest = new LoginRequestDto("test@example.com", "Password@123");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(validRequest);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when email is null")
    void loginRequestDto_ShouldFailValidation_WhenEmailIsNull() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto(null, "Password@123");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when email is blank")
    void loginRequestDto_ShouldFailValidation_WhenEmailIsBlank() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("", "Password@123");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when email is whitespace only")
    void loginRequestDto_ShouldFailValidation_WhenEmailIsWhitespaceOnly() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("   ", "Password@123");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size()); // expect both violations
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("Email is required"))
        );
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format"))
        );
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when email format is invalid")
    void loginRequestDto_ShouldFailValidation_WhenEmailFormatIsInvalid() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("invalid-email", "Password@123");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when email format is invalid (no @)")
    void loginRequestDto_ShouldFailValidation_WhenEmailFormatIsInvalidNoAt() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("testexample.com", "Password@123");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when email format is invalid (no domain)")
    void loginRequestDto_ShouldFailValidation_WhenEmailFormatIsInvalidNoDomain() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("test@", "Password@123");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when password is null")
    void loginRequestDto_ShouldFailValidation_WhenPasswordIsNull() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("test@example.com", null);

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when password is blank")
    void loginRequestDto_ShouldFailValidation_WhenPasswordIsBlank() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("test@example.com", "");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when password is whitespace only")
    void loginRequestDto_ShouldFailValidation_WhenPasswordIsWhitespaceOnly() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("test@example.com", "   ");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("LoginRequestDto - Should fail validation when both email and password are invalid")
    void loginRequestDto_ShouldFailValidation_WhenBothEmailAndPasswordAreInvalid() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto("invalid-email", "");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        
        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());
        
        assertTrue(messages.contains("Invalid email format"));
        assertTrue(messages.contains("Password is required"));
    }

    // ========== SIGNUP REQUEST DTO VALIDATION TESTS ==========

    @Test
    @DisplayName("SignUpRequestDto - Should pass validation with valid data")
    void signUpRequestDto_ShouldPassValidation_WithValidData() {
        // Arrange
        SignUpRequestDto validRequest = new SignUpRequestDto("test@example.com", "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(validRequest);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when email is null")
    void signUpRequestDto_ShouldFailValidation_WhenEmailIsNull() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto(null, "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when email is blank")
    void signUpRequestDto_ShouldFailValidation_WhenEmailIsBlank() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("", "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when email is whitespace only")
    void signUpRequestDto_ShouldFailValidation_WhenEmailIsWhitespaceOnly() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("   ", "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("Email is required"))
        );
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format"))
        );
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when email format is invalid")
    void signUpRequestDto_ShouldFailValidation_WhenEmailFormatIsInvalid() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("invalid-email", "Password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when password is null")
    void signUpRequestDto_ShouldFailValidation_WhenPasswordIsNull() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", null);

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when password is empty")
    void signUpRequestDto_ShouldFailValidation_WhenPasswordIsEmpty() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("Password is required"))
        );
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when password is too short")
    void signUpRequestDto_ShouldFailValidation_WhenPasswordIsTooShort() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "Pass@1");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when password has no uppercase")
    void signUpRequestDto_ShouldFailValidation_WhenPasswordHasNoUppercase() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "password@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", 
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when password has no lowercase")
    void signUpRequestDto_ShouldFailValidation_WhenPasswordHasNoLowercase() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "PASSWORD@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", 
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when password has no number")
    void signUpRequestDto_ShouldFailValidation_WhenPasswordHasNoNumber() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "Password@");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", 
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when password has no special character")
    void signUpRequestDto_ShouldFailValidation_WhenPasswordHasNoSpecialCharacter() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "Password123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character", 
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should pass validation with valid password containing different special characters")
    void signUpRequestDto_ShouldPassValidation_WithValidPasswordContainingDifferentSpecialCharacters() {
        // Arrange
        SignUpRequestDto request1 = new SignUpRequestDto("test@example.com", "Password#123");
        SignUpRequestDto request2 = new SignUpRequestDto("test@example.com", "Password$123");
        SignUpRequestDto request3 = new SignUpRequestDto("test@example.com", "Password!123");
        SignUpRequestDto request4 = new SignUpRequestDto("test@example.com", "Password%123");
        SignUpRequestDto request5 = new SignUpRequestDto("test@example.com", "Password*123");
        SignUpRequestDto request6 = new SignUpRequestDto("test@example.com", "Password?123");
        SignUpRequestDto request7 = new SignUpRequestDto("test@example.com", "Password&123");

        // Act & Assert
        assertTrue(validator.validate(request1).isEmpty());
        assertTrue(validator.validate(request2).isEmpty());
        assertTrue(validator.validate(request3).isEmpty());
        assertTrue(validator.validate(request4).isEmpty());
        assertTrue(validator.validate(request5).isEmpty());
        assertTrue(validator.validate(request6).isEmpty());
        assertTrue(validator.validate(request7).isEmpty());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should fail validation when both email and password are invalid")
    void signUpRequestDto_ShouldFailValidation_WhenBothEmailAndPasswordAreInvalid() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("invalid-email", "weak");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        
        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());
        
        assertTrue(messages.contains("Invalid email format"));
        assertTrue(messages.contains("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"));
    }

    // ========== EDGE CASES AND BOUNDARY TESTS ==========

    @Test
    @DisplayName("LoginRequestDto - Should pass validation with maximum length email")
    void loginRequestDto_ShouldPassValidation_WithMaximumLengthEmail() {
        // Arrange
        String longEmail = "a".repeat(50) + "@" + "b".repeat(50) + ".com";
        LoginRequestDto request = new LoginRequestDto(longEmail, "Password@123");

        // Act
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should pass validation with maximum length password")
    void signUpRequestDto_ShouldPassValidation_WithMaximumLengthPassword() {
        // Arrange
        String longPassword = "Password@123" + "a".repeat(100);
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", longPassword);

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("SignUpRequestDto - Should pass validation with minimum valid password")
    void signUpRequestDto_ShouldPassValidation_WithMinimumValidPassword() {
        // Arrange
        SignUpRequestDto request = new SignUpRequestDto("test@example.com", "Pass@123");

        // Act
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }
}
