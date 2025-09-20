package incubyte_project_backend.dto.request.sweet;

import com.incubyte.incubyte_project_backend.dto.request.sweet.AddSweetRequest;
import com.incubyte.incubyte_project_backend.dto.request.sweet.PurchaseRequest;
import com.incubyte.incubyte_project_backend.dto.request.sweet.RestockRequest;
import com.incubyte.incubyte_project_backend.dto.request.sweet.SearchSweetRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Sweet Request Validation Tests")
class SweetRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("AddSweetRequest - Should pass validation with valid data")
    void addSweetRequest_ShouldPassValidation_WithValidData() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "Chocolate", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Valid request should not have violations");
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when name is null")
    void addSweetRequest_ShouldFailValidation_WhenNameIsNull() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest(null, "Chocolate", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when name is blank")
    void addSweetRequest_ShouldFailValidation_WhenNameIsBlank() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("", "Chocolate", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when name is too short")
    void addSweetRequest_ShouldFailValidation_WhenNameIsTooShort() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("AB", "Chocolate", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name must be between 3 and 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when name is too long")
    void addSweetRequest_ShouldFailValidation_WhenNameIsTooLong() {
        // Arrange
        String longName = "A".repeat(51); // 51 characters
        AddSweetRequest request = new AddSweetRequest(longName, "Chocolate", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name must be between 3 and 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when name contains numbers")
    void addSweetRequest_ShouldFailValidation_WhenNameContainsNumbers() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate123", "Chocolate", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name must not contain numbers or special characters", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when name contains invalid special characters")
    void addSweetRequest_ShouldFailValidation_WhenNameContainsInvalidSpecialCharacters() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate@Bar", "Chocolate", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name must not contain numbers or special characters", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should pass validation when name contains allowed special characters")
    void addSweetRequest_ShouldPassValidation_WhenNameContainsAllowedSpecialCharacters() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate & Caramel-Bar", "Chocolate", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Name with allowed special characters should pass validation");
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when category is null")
    void addSweetRequest_ShouldFailValidation_WhenCategoryIsNull() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", null, 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Category is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when category is blank")
    void addSweetRequest_ShouldFailValidation_WhenCategoryIsBlank() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        assertEquals("Category is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when category is too short")
    void addSweetRequest_ShouldFailValidation_WhenCategoryIsTooShort() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "A", 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Category must be between 2 and 50 characters",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when category is too long")
    void addSweetRequest_ShouldFailValidation_WhenCategoryIsTooLong() {
        // Arrange
        String longCategory = "A".repeat(51); // 51 characters, exceeding max length
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", longCategory, 2.50, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Category must be between 2 and 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when price is null")
    void addSweetRequest_ShouldFailValidation_WhenPriceIsNull() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "Chocolate", null, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when price is zero")
    void addSweetRequest_ShouldFailValidation_WhenPriceIsZero() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "Chocolate", 0.0, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when price is negative")
    void addSweetRequest_ShouldFailValidation_WhenPriceIsNegative() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "Chocolate", -1.0, 100);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when quantity is null")
    void addSweetRequest_ShouldFailValidation_WhenQuantityIsNull() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "Chocolate", 2.50, null);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when quantity is zero")
    void addSweetRequest_ShouldFailValidation_WhenQuantityIsZero() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "Chocolate", 2.50, 0);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation when quantity is negative")
    void addSweetRequest_ShouldFailValidation_WhenQuantityIsNegative() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("Chocolate Bar", "Chocolate", 2.50, -1);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("AddSweetRequest - Should fail validation with multiple violations")
    void addSweetRequest_ShouldFailValidation_WithMultipleViolations() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("", "", -1.0, -1);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(8, violations.size());
        
        // Check that all expected violations are present
        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());



        assertTrue(messages.contains("Name is required"));
        assertTrue(messages.contains("Name must not contain numbers or special characters"));
        assertTrue(messages.contains("Category must be between 2 and 50 characters"));
        assertTrue(messages.contains("Name must be between 3 and 50 characters"));
        assertTrue(messages.contains("Category must not contain numbers or special characters"));
        assertTrue(messages.contains("Category is required"));
        assertTrue(messages.contains("Price must be greater than 0"));
        assertTrue(messages.contains("Quantity must be at least 1"));
    }

    @Test
    @DisplayName("PurchaseRequest - Should pass validation with valid data")
    void purchaseRequest_ShouldPassValidation_WithValidData() {
        // Arrange
        PurchaseRequest request = new PurchaseRequest(5);

        // Act
        Set<ConstraintViolation<PurchaseRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Valid request should not have violations");
    }

    @Test
    @DisplayName("PurchaseRequest - Should fail validation when quantity is null")
    void purchaseRequest_ShouldFailValidation_WhenQuantityIsNull() {
        // Arrange
        PurchaseRequest request = new PurchaseRequest(null);

        // Act
        Set<ConstraintViolation<PurchaseRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("PurchaseRequest - Should fail validation when quantity is zero")
    void purchaseRequest_ShouldFailValidation_WhenQuantityIsZero() {
        // Arrange
        PurchaseRequest request = new PurchaseRequest(0);

        // Act
        Set<ConstraintViolation<PurchaseRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("PurchaseRequest - Should fail validation when quantity is negative")
    void purchaseRequest_ShouldFailValidation_WhenQuantityIsNegative() {
        // Arrange
        PurchaseRequest request = new PurchaseRequest(-1);

        // Act
        Set<ConstraintViolation<PurchaseRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("RestockRequest - Should pass validation with valid data")
    void restockRequest_ShouldPassValidation_WithValidData() {
        // Arrange
        RestockRequest request = new RestockRequest(50);

        // Act
        Set<ConstraintViolation<RestockRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Valid request should not have violations");
    }

    @Test
    @DisplayName("RestockRequest - Should fail validation when quantity is null")
    void restockRequest_ShouldFailValidation_WhenQuantityIsNull() {
        // Arrange
        RestockRequest request = new RestockRequest(null);

        // Act
        Set<ConstraintViolation<RestockRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("RestockRequest - Should fail validation when quantity is zero")
    void restockRequest_ShouldFailValidation_WhenQuantityIsZero() {
        // Arrange
        RestockRequest request = new RestockRequest(0);

        // Act
        Set<ConstraintViolation<RestockRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("RestockRequest - Should fail validation when quantity is negative")
    void restockRequest_ShouldFailValidation_WhenQuantityIsNegative() {
        // Arrange
        RestockRequest request = new RestockRequest(-1);

        // Act
        Set<ConstraintViolation<RestockRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Quantity must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SearchSweetRequest - Should pass validation with valid data")
    void searchSweetRequest_ShouldPassValidation_WithValidData() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setName("Chocolate");
        request.setCategory("Chocolate");
        request.setMinPrice(1.0);
        request.setMaxPrice(5.0);

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Valid request should not have violations");
    }

    @Test
    @DisplayName("SearchSweetRequest - Should pass validation with null optional fields")
    void searchSweetRequest_ShouldPassValidation_WithNullOptionalFields() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        // All fields are null, which should be valid for search

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Search request with null fields should be valid");
    }

    @Test
    @DisplayName("SearchSweetRequest - Should fail validation when minPrice is negative")
    void searchSweetRequest_ShouldFailValidation_WhenMinPriceIsNegative() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setMinPrice(-1.0);

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Min price must be 0 or greater", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SearchSweetRequest - Should fail validation when maxPrice is zero")
    void searchSweetRequest_ShouldFailValidation_WhenMaxPriceIsZero() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setMaxPrice(0.0);

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SearchSweetRequest - Should fail validation when maxPrice is negative")
    void searchSweetRequest_ShouldFailValidation_WhenMaxPriceIsNegative() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setMaxPrice(-1.0);

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("SearchSweetRequest - Should pass validation when minPrice is zero")
    void searchSweetRequest_ShouldPassValidation_WhenMinPriceIsZero() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setMinPrice(0.0);

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Min price of 0 should be valid");
    }

    @Test
    @DisplayName("SearchSweetRequest - Should pass validation with only name filter")
    void searchSweetRequest_ShouldPassValidation_WithOnlyNameFilter() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setName("Chocolate");

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Search with only name should be valid");
    }

    @Test
    @DisplayName("SearchSweetRequest - Should pass validation with only category filter")
    void searchSweetRequest_ShouldPassValidation_WithOnlyCategoryFilter() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setCategory("Chocolate");

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Search with only category should be valid");
    }

    @Test
    @DisplayName("SearchSweetRequest - Should pass validation with only price range")
    void searchSweetRequest_ShouldPassValidation_WithOnlyPriceRange() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setMinPrice(1.0);
        request.setMaxPrice(5.0);

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Search with only price range should be valid");
    }

    @Test
    @DisplayName("SearchSweetRequest - Should fail validation with multiple price violations")
    void searchSweetRequest_ShouldFailValidation_WithMultiplePriceViolations() {
        // Arrange
        SearchSweetRequest request = new SearchSweetRequest();
        request.setMinPrice(-1.0);
        request.setMaxPrice(-2.0);

        // Act
        Set<ConstraintViolation<SearchSweetRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        
        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());
        
        assertTrue(messages.contains("Min price must be 0 or greater"));
        assertTrue(messages.contains("Price must be greater than 0"));
    }

    @Test
    @DisplayName("AddSweetRequest - Should pass validation with edge case values")
    void addSweetRequest_ShouldPassValidation_WithEdgeCaseValues() {
        // Arrange
        AddSweetRequest request = new AddSweetRequest("ABC", "AB", 0.01, 1);

        // Act
        Set<ConstraintViolation<AddSweetRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Edge case values should pass validation");
    }

    @Test
    @DisplayName("PurchaseRequest - Should pass validation with large quantity")
    void purchaseRequest_ShouldPassValidation_WithLargeQuantity() {
        // Arrange
        PurchaseRequest request = new PurchaseRequest(Integer.MAX_VALUE);

        // Act
        Set<ConstraintViolation<PurchaseRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Large quantity should pass validation");
    }

    @Test
    @DisplayName("RestockRequest - Should pass validation with large quantity")
    void restockRequest_ShouldPassValidation_WithLargeQuantity() {
        // Arrange
        RestockRequest request = new RestockRequest(Integer.MAX_VALUE);

        // Act
        Set<ConstraintViolation<RestockRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Large quantity should pass validation");
    }
}
