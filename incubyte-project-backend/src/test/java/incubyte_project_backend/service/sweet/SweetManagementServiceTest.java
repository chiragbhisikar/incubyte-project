package incubyte_project_backend.service.sweet;

import com.incubyte.incubyte_project_backend.dto.request.sweet.UpdateSweetRequest;
import com.incubyte.incubyte_project_backend.entity.Sweet;
import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetRepository;
import com.incubyte.incubyte_project_backend.service.sweet.SweetManagementServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Sweet Management Service Tests")
class SweetManagementServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetManagementServiceimpl sweetManagementService;

    private Sweet testSweet;
    private UUID testSweetId;

    @BeforeEach
    void setUp() {
        testSweetId = UUID.randomUUID();
        
        testSweet = Sweet.builder()
                .id(testSweetId)
                .name("Chocolate Bar")
                .category("Chocolate")
                .price(2.50)
                .quantity(100)
                .build();
    }

    @Test
    @DisplayName("addSweet - Should return saved sweet when valid sweet is provided")
    void addSweet_ShouldReturnSavedSweet_WhenValidSweetProvided() {
        // Arrange
        Sweet newSweet = Sweet.builder()
                .name("New Chocolate Bar")
                .category("Chocolate")
                .price(3.00)
                .quantity(50)
                .build();
        
        Sweet savedSweet = Sweet.builder()
                .id(UUID.randomUUID())
                .name("New Chocolate Bar")
                .category("Chocolate")
                .price(3.00)
                .quantity(50)
                .build();
        
        when(sweetRepository.save(any(Sweet.class))).thenReturn(savedSweet);

        // Act
        Sweet result = sweetManagementService.addSweet(newSweet);

        // Assert
        assertNotNull(result);
        assertEquals(savedSweet.getId(), result.getId());
        assertEquals(savedSweet.getName(), result.getName());
        assertEquals(savedSweet.getCategory(), result.getCategory());
        assertEquals(savedSweet.getPrice(), result.getPrice());
        assertEquals(savedSweet.getQuantity(), result.getQuantity());
        verify(sweetRepository, times(1)).save(newSweet);
    }

    @Test
    @DisplayName("addSweet - Should handle null sweet")
    void addSweet_ShouldHandleNullSweet() {
        // Arrange
        when(sweetRepository.save(null)).thenThrow(new IllegalArgumentException("Sweet cannot be null"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sweetManagementService.addSweet(null);
        });

        assertEquals("Sweet cannot be null", exception.getMessage());
        verify(sweetRepository, times(1)).save(null);
    }

    @Test
    @DisplayName("addSweet - Should handle sweet with null fields")
    void addSweet_ShouldHandleSweetWithNullFields() {
        // Arrange
        Sweet sweetWithNullFields = Sweet.builder()
                .name(null)
                .category(null)
                .price(null)
                .quantity(null)
                .build();
        
        when(sweetRepository.save(any(Sweet.class))).thenReturn(sweetWithNullFields);

        // Act
        Sweet result = sweetManagementService.addSweet(sweetWithNullFields);

        // Assert
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getCategory());
        assertNull(result.getPrice());
        assertNull(result.getQuantity());
        verify(sweetRepository, times(1)).save(sweetWithNullFields);
    }

    @Test
    @DisplayName("addSweet - Should handle repository exception")
    void addSweet_ShouldHandleRepositoryException() {
        // Arrange
        Sweet newSweet = Sweet.builder()
                .name("New Sweet")
                .category("Test")
                .price(1.0)
                .quantity(10)
                .build();
        
        when(sweetRepository.save(any(Sweet.class))).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetManagementService.addSweet(newSweet);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetRepository, times(1)).save(newSweet);
    }

    @Test
    @DisplayName("updateSweet - Should return updated sweet when valid update request is provided")
    void updateSweet_ShouldReturnUpdatedSweet_WhenValidUpdateRequestProvided() throws SweetNotFoundException {
        // Arrange
        UpdateSweetRequest updateRequest = new UpdateSweetRequest(
                "Updated Chocolate Bar",
                "Premium Chocolate",
                3.50,
                150
        );
        
        Sweet updatedSweet = Sweet.builder()
                .id(testSweetId)
                .name("Updated Chocolate Bar")
                .category("Premium Chocolate")
                .price(3.50)
                .quantity(150)
                .build();
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(updatedSweet);

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updatedSweet.getName(), result.getName());
        assertEquals(updatedSweet.getCategory(), result.getCategory());
        assertEquals(updatedSweet.getPrice(), result.getPrice());
        assertEquals(updatedSweet.getQuantity(), result.getQuantity());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("updateSweet - Should throw SweetNotFoundException when sweet ID is invalid")
    void updateSweet_ShouldThrowSweetNotFoundException_WhenSweetIdIsInvalid() {
        // Arrange
        UUID invalidId = UUID.randomUUID();
        UpdateSweetRequest updateRequest = new UpdateSweetRequest(
                "Updated Name",
                "Updated Category",
                3.0,
                100
        );
        
        when(sweetRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetManagementService.updateSweet(invalidId, updateRequest);
        });

        assertEquals("Sweet not found with id: " + invalidId, exception.getMessage());
        verify(sweetRepository, times(1)).findById(invalidId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("updateSweet - Should handle partial update with null fields")
    void updateSweet_ShouldHandlePartialUpdateWithNullFields() throws SweetNotFoundException {
        // Arrange
        UpdateSweetRequest partialUpdateRequest = new UpdateSweetRequest(
                "Updated Name Only",
                null,
                null,
                null
        );
        
        Sweet updatedSweet = Sweet.builder()
                .id(testSweetId)
                .name("Updated Name Only")
                .category("Chocolate") // Should remain unchanged
                .price(2.50) // Should remain unchanged
                .quantity(100) // Should remain unchanged
                .build();
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(updatedSweet);

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, partialUpdateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name Only", result.getName());
        assertEquals("Chocolate", result.getCategory()); // Should remain unchanged
        assertEquals(2.50, result.getPrice()); // Should remain unchanged
        assertEquals(100, result.getQuantity()); // Should remain unchanged
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("updateSweet - Should handle update with only category")
    void updateSweet_ShouldHandleUpdateWithOnlyCategory() throws SweetNotFoundException {
        // Arrange
        UpdateSweetRequest categoryOnlyUpdateRequest = new UpdateSweetRequest(
                null,
                "Updated Category",
                null,
                null
        );
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, categoryOnlyUpdateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Chocolate Bar", result.getName()); // Should remain unchanged
        assertEquals("Updated Category", result.getCategory());
        assertEquals(2.50, result.getPrice()); // Should remain unchanged
        assertEquals(100, result.getQuantity()); // Should remain unchanged
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("updateSweet - Should handle update with only price")
    void updateSweet_ShouldHandleUpdateWithOnlyPrice() throws SweetNotFoundException {
        // Arrange
        UpdateSweetRequest priceOnlyUpdateRequest = new UpdateSweetRequest(
                null,
                null,
                4.00,
                null
        );
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, priceOnlyUpdateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Chocolate Bar", result.getName()); // Should remain unchanged
        assertEquals("Chocolate", result.getCategory()); // Should remain unchanged
        assertEquals(4.00, result.getPrice());
        assertEquals(100, result.getQuantity()); // Should remain unchanged
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("updateSweet - Should handle update with only quantity")
    void updateSweet_ShouldHandleUpdateWithOnlyQuantity() throws SweetNotFoundException {
        // Arrange
        UpdateSweetRequest quantityOnlyUpdateRequest = new UpdateSweetRequest(
                null,
                null,
                null,
                200
        );
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, quantityOnlyUpdateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Chocolate Bar", result.getName()); // Should remain unchanged
        assertEquals("Chocolate", result.getCategory()); // Should remain unchanged
        assertEquals(2.50, result.getPrice()); // Should remain unchanged
        assertEquals(200, result.getQuantity());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("updateSweet - Should handle null update request")
    void updateSweet_ShouldHandleNullUpdateRequest() throws SweetNotFoundException {
        // Arrange
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, null);

        // Assert
        assertNotNull(result);
        assertEquals("Chocolate Bar", result.getName()); // Should remain unchanged
        assertEquals("Chocolate", result.getCategory()); // Should remain unchanged
        assertEquals(2.50, result.getPrice()); // Should remain unchanged
        assertEquals(100, result.getQuantity()); // Should remain unchanged
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, never()).save(any(Sweet.class)); // Should not save when request is null
    }

    @Test
    @DisplayName("updateSweet - Should handle repository exception during findById")
    void updateSweet_ShouldHandleRepositoryExceptionDuringFindById() {
        // Arrange
        UpdateSweetRequest updateRequest = new UpdateSweetRequest(
                "Updated Name",
                "Updated Category",
                3.0,
                100
        );
        
        when(sweetRepository.findById(testSweetId)).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetManagementService.updateSweet(testSweetId, updateRequest);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("updateSweet - Should handle repository exception during save")
    void updateSweet_ShouldHandleRepositoryExceptionDuringSave() {
        // Arrange
        UpdateSweetRequest updateRequest = new UpdateSweetRequest(
                "Updated Name",
                "Updated Category",
                3.0,
                100
        );
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenThrow(new RuntimeException("Database save failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetManagementService.updateSweet(testSweetId, updateRequest);
        });

        assertEquals("Database save failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("deleteSweet - Should delete sweet when valid ID is provided")
    void deleteSweet_ShouldDeleteSweet_WhenValidIdProvided() throws SweetNotFoundException {
        // Arrange
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        doNothing().when(sweetRepository).delete(any(Sweet.class));

        // Act
        sweetManagementService.deleteSweet(testSweetId);

        // Assert
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).delete(testSweet);
    }

    @Test
    @DisplayName("deleteSweet - Should throw SweetNotFoundException when sweet ID is invalid")
    void deleteSweet_ShouldThrowSweetNotFoundException_WhenSweetIdIsInvalid() {
        // Arrange
        UUID invalidId = UUID.randomUUID();
        when(sweetRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetManagementService.deleteSweet(invalidId);
        });

        assertEquals("Sweet not found with id: " + invalidId, exception.getMessage());
        verify(sweetRepository, times(1)).findById(invalidId);
        verify(sweetRepository, never()).delete(any(Sweet.class));
    }

    @Test
    @DisplayName("deleteSweet - Should handle null sweet ID")
    void deleteSweet_ShouldHandleNullSweetId() {
        // Arrange
        when(sweetRepository.findById(null)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetManagementService.deleteSweet(null);
        });

        assertEquals("Sweet not found with id: null", exception.getMessage());
        verify(sweetRepository, times(1)).findById(null);
        verify(sweetRepository, never()).delete(any(Sweet.class));
    }

    @Test
    @DisplayName("deleteSweet - Should handle repository exception during findById")
    void deleteSweet_ShouldHandleRepositoryExceptionDuringFindById() {
        // Arrange
        when(sweetRepository.findById(testSweetId)).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetManagementService.deleteSweet(testSweetId);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, never()).delete(any(Sweet.class));
    }

    @Test
    @DisplayName("deleteSweet - Should handle repository exception during delete")
    void deleteSweet_ShouldHandleRepositoryExceptionDuringDelete() {
        // Arrange
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        doThrow(new RuntimeException("Database delete failed")).when(sweetRepository).delete(any(Sweet.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetManagementService.deleteSweet(testSweetId);
        });

        assertEquals("Database delete failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).delete(testSweet);
    }

    @Test
    @DisplayName("updateSweet - Should verify all fields are updated correctly")
    void updateSweet_ShouldVerifyAllFieldsAreUpdatedCorrectly() throws SweetNotFoundException {
        // Arrange
        UpdateSweetRequest updateRequest = new UpdateSweetRequest(
                "Completely New Name",
                "Completely New Category",
                5.99,
                75
        );
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(argThat(sweet -> 
            "Completely New Name".equals(sweet.getName()) &&
            "Completely New Category".equals(sweet.getCategory()) &&
            sweet.getPrice().equals(5.99) &&
            sweet.getQuantity().equals(75) &&
            sweet.getId().equals(testSweetId)
        ));
    }

    @Test
    @DisplayName("updateSweet - Should handle edge case with empty string values")
    void updateSweet_ShouldHandleEdgeCaseWithEmptyStringValues() throws SweetNotFoundException {
        // Arrange
        UpdateSweetRequest updateRequest = new UpdateSweetRequest(
                "",
                "",
                0.0,
                0
        );
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(argThat(sweet -> 
            "".equals(sweet.getName()) &&
            "".equals(sweet.getCategory()) &&
            sweet.getPrice().equals(0.0) &&
            sweet.getQuantity().equals(0) &&
            sweet.getId().equals(testSweetId)
        ));
    }

    @Test
    @DisplayName("updateSweet - Should handle edge case with very large values")
    void updateSweet_ShouldHandleEdgeCaseWithVeryLargeValues() throws SweetNotFoundException {
        // Arrange
        UpdateSweetRequest updateRequest = new UpdateSweetRequest(
                "Very Long Name That Exceeds Normal Length And Might Cause Issues",
                "Very Long Category Name",
                Double.MAX_VALUE,
                Integer.MAX_VALUE
        );
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetManagementService.updateSweet(testSweetId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(argThat(sweet -> 
            "Very Long Name That Exceeds Normal Length And Might Cause Issues".equals(sweet.getName()) &&
            "Very Long Category Name".equals(sweet.getCategory()) &&
            sweet.getPrice().equals(Double.MAX_VALUE) &&
            sweet.getQuantity().equals(Integer.MAX_VALUE) &&
            sweet.getId().equals(testSweetId)
        ));
    }

    @Test
    @DisplayName("addSweet - Should handle sweet with special characters")
    void addSweet_ShouldHandleSweetWithSpecialCharacters() {
        // Arrange
        Sweet sweetWithSpecialChars = Sweet.builder()
                .name("Chocolate & Caramel Bar (Premium)")
                .category("Chocolate-Caramel")
                .price(3.99)
                .quantity(25)
                .build();
        
        when(sweetRepository.save(any(Sweet.class))).thenReturn(sweetWithSpecialChars);

        // Act
        Sweet result = sweetManagementService.addSweet(sweetWithSpecialChars);

        // Assert
        assertNotNull(result);
        assertEquals("Chocolate & Caramel Bar (Premium)", result.getName());
        assertEquals("Chocolate-Caramel", result.getCategory());
        assertEquals(3.99, result.getPrice());
        assertEquals(25, result.getQuantity());
        verify(sweetRepository, times(1)).save(sweetWithSpecialChars);
    }
}
