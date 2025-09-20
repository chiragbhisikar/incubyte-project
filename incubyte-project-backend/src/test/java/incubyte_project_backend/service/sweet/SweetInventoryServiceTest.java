package incubyte_project_backend.service.sweet;

import com.incubyte.incubyte_project_backend.dto.response.sweet.SweetSoldResponse;
import com.incubyte.incubyte_project_backend.entity.Sweet;
import com.incubyte.incubyte_project_backend.exception.NotEnoughStockException;
import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetRepository;
import com.incubyte.incubyte_project_backend.service.sweet.SweetInventoryServiceImpl;
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
@DisplayName("Sweet Inventory Service Tests")
class SweetInventoryServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetInventoryServiceImpl sweetInventoryService;

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
    @DisplayName("purchaseSweet - Should return SweetSoldResponse when valid purchase is made")
    void purchaseSweet_ShouldReturnSweetSoldResponse_WhenValidPurchaseMade() throws SweetNotFoundException, NotEnoughStockException {
        // Arrange
        int purchaseQuantity = 5;
        int expectedRemainingQuantity = 95;
        double expectedTotalAmount = 12.50; // 5 * 2.50
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        SweetSoldResponse result = sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet, result.getSweet());
        assertEquals(expectedRemainingQuantity, result.getQuantity());
        assertEquals(expectedTotalAmount, result.getTotalAmount(), 0.01);
        
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should throw SweetNotFoundException when sweet ID is invalid")
    void purchaseSweet_ShouldThrowSweetNotFoundException_WhenSweetIdIsInvalid() {
        // Arrange
        UUID invalidId = UUID.randomUUID();
        int purchaseQuantity = 5;
        
        when(sweetRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetInventoryService.purchaseSweet(invalidId, purchaseQuantity);
        });

        assertEquals("Sweet not found with id: " + invalidId, exception.getMessage());
        verify(sweetRepository, times(1)).findById(invalidId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should throw NotEnoughStockException when requested quantity exceeds available stock")
    void purchaseSweet_ShouldThrowNotEnoughStockException_WhenRequestedQuantityExceedsAvailableStock() {
        // Arrange
        int purchaseQuantity = 150; // More than available quantity (100)
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));

        // Act & Assert
        NotEnoughStockException exception = assertThrows(NotEnoughStockException.class, () -> {
            sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);
        });

        assertEquals("Sweet Has Not Enough Quantity We Can Provide Only 100 Quantities !", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should handle exact quantity match")
    void purchaseSweet_ShouldHandleExactQuantityMatch() throws SweetNotFoundException, NotEnoughStockException {
        // Arrange
        int purchaseQuantity = 100; // Exact available quantity
        int expectedRemainingQuantity = 0;
        double expectedTotalAmount = 250.0; // 100 * 2.50
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        SweetSoldResponse result = sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet, result.getSweet());
        assertEquals(expectedRemainingQuantity, result.getQuantity());
        assertEquals(expectedTotalAmount, result.getTotalAmount(), 0.01);
        
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should handle zero quantity purchase")
    void purchaseSweet_ShouldHandleZeroQuantityPurchase() throws SweetNotFoundException, NotEnoughStockException {
        // Arrange
        int purchaseQuantity = 0;
        int expectedRemainingQuantity = 100; // No change
        double expectedTotalAmount = 0.0; // 0 * 2.50
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        SweetSoldResponse result = sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet, result.getSweet());
        assertEquals(expectedRemainingQuantity, result.getQuantity());
        assertEquals(expectedTotalAmount, result.getTotalAmount(), 0.01);
        
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should handle out of stock sweet")
    void purchaseSweet_ShouldHandleOutOfStockSweet() {
        // Arrange
        Sweet outOfStockSweet = Sweet.builder()
                .id(testSweetId)
                .name("Out of Stock Sweet")
                .category("Test")
                .price(1.0)
                .quantity(0)
                .build();
        
        int purchaseQuantity = 1;
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(outOfStockSweet));

        // Act & Assert
        NotEnoughStockException exception = assertThrows(NotEnoughStockException.class, () -> {
            sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);
        });

        assertEquals("Sweet Has Not Enough Quantity We Can Provide Only 0 Quantities !", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should handle negative quantity")
    void purchaseSweet_ShouldHandleNegativeQuantity() throws SweetNotFoundException, NotEnoughStockException {
        // Arrange
        int purchaseQuantity = -5;
        int expectedRemainingQuantity = 105; // 100 - (-5) = 105
        double expectedTotalAmount = -12.50; // -5 * 2.50
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        SweetSoldResponse result = sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet, result.getSweet());
        assertEquals(expectedRemainingQuantity, result.getQuantity());
        assertEquals(expectedTotalAmount, result.getTotalAmount(), 0.01);
        
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should handle decimal price calculation")
    void purchaseSweet_ShouldHandleDecimalPriceCalculation() throws SweetNotFoundException, NotEnoughStockException {
        // Arrange
        Sweet decimalPriceSweet = Sweet.builder()
                .id(testSweetId)
                .name("Decimal Price Sweet")
                .category("Test")
                .price(1.33)
                .quantity(100)
                .build();
        
        int purchaseQuantity = 3;
        double expectedTotalAmount = 3.99; // 3 * 1.33
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(decimalPriceSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(decimalPriceSweet);

        // Act
        SweetSoldResponse result = sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(decimalPriceSweet, result.getSweet());
        assertEquals(97, result.getQuantity());
        assertEquals(expectedTotalAmount, result.getTotalAmount(), 0.01);
        
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should return updated sweet when valid restock is made")
    void restockSweet_ShouldReturnUpdatedSweet_WhenValidRestockMade() throws SweetNotFoundException {
        // Arrange
        int restockQuantity = 50;
        int expectedTotalQuantity = 150; // 100 + 50
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetInventoryService.restockSweet(testSweetId, restockQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet, result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should throw SweetNotFoundException when sweet ID is invalid")
    void restockSweet_ShouldThrowSweetNotFoundException_WhenSweetIdIsInvalid() {
        // Arrange
        UUID invalidId = UUID.randomUUID();
        int restockQuantity = 50;
        
        when(sweetRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetInventoryService.restockSweet(invalidId, restockQuantity);
        });

        assertEquals("Sweet not found with id: " + invalidId, exception.getMessage());
        verify(sweetRepository, times(1)).findById(invalidId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should handle zero restock quantity")
    void restockSweet_ShouldHandleZeroRestockQuantity() throws SweetNotFoundException {
        // Arrange
        int restockQuantity = 0;
        int expectedTotalQuantity = 100; // 100 + 0
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetInventoryService.restockSweet(testSweetId, restockQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet, result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should handle negative restock quantity")
    void restockSweet_ShouldHandleNegativeRestockQuantity() throws SweetNotFoundException {
        // Arrange
        int restockQuantity = -20;
        int expectedTotalQuantity = 80; // 100 + (-20)
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetInventoryService.restockSweet(testSweetId, restockQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet, result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should handle large restock quantity")
    void restockSweet_ShouldHandleLargeRestockQuantity() throws SweetNotFoundException {
        // Arrange
        int restockQuantity = 1000;
        int expectedTotalQuantity = 1100; // 100 + 1000
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(testSweet);

        // Act
        Sweet result = sweetInventoryService.restockSweet(testSweetId, restockQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet, result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should handle out of stock sweet restock")
    void restockSweet_ShouldHandleOutOfStockSweetRestock() throws SweetNotFoundException {
        // Arrange
        Sweet outOfStockSweet = Sweet.builder()
                .id(testSweetId)
                .name("Out of Stock Sweet")
                .category("Test")
                .price(1.0)
                .quantity(0)
                .build();
        
        int restockQuantity = 50;
        int expectedTotalQuantity = 50; // 0 + 50
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(outOfStockSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(outOfStockSweet);

        // Act
        Sweet result = sweetInventoryService.restockSweet(testSweetId, restockQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(outOfStockSweet, result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should handle repository exception during findById")
    void purchaseSweet_ShouldHandleRepositoryExceptionDuringFindById() {
        // Arrange
        int purchaseQuantity = 5;
        
        when(sweetRepository.findById(testSweetId)).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should handle repository exception during save")
    void purchaseSweet_ShouldHandleRepositoryExceptionDuringSave() {
        // Arrange
        int purchaseQuantity = 5;
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenThrow(new RuntimeException("Database save failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);
        });

        assertEquals("Database save failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should handle repository exception during findById")
    void restockSweet_ShouldHandleRepositoryExceptionDuringFindById() {
        // Arrange
        int restockQuantity = 50;
        
        when(sweetRepository.findById(testSweetId)).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetInventoryService.restockSweet(testSweetId, restockQuantity);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should handle repository exception during save")
    void restockSweet_ShouldHandleRepositoryExceptionDuringSave() {
        // Arrange
        int restockQuantity = 50;
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenThrow(new RuntimeException("Database save failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetInventoryService.restockSweet(testSweetId, restockQuantity);
        });

        assertEquals("Database save failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should handle null sweet ID")
    void purchaseSweet_ShouldHandleNullSweetId() {
        // Arrange
        int purchaseQuantity = 5;
        
        when(sweetRepository.findById(null)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetInventoryService.purchaseSweet(null, purchaseQuantity);
        });

        assertEquals("Sweet not found with id: null", exception.getMessage());
        verify(sweetRepository, times(1)).findById(null);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("restockSweet - Should handle null sweet ID")
    void restockSweet_ShouldHandleNullSweetId() {
        // Arrange
        int restockQuantity = 50;
        
        when(sweetRepository.findById(null)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetInventoryService.restockSweet(null, restockQuantity);
        });

        assertEquals("Sweet not found with id: null", exception.getMessage());
        verify(sweetRepository, times(1)).findById(null);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    @DisplayName("purchaseSweet - Should verify quantity is updated correctly")
    void purchaseSweet_ShouldVerifyQuantityIsUpdatedCorrectly() throws SweetNotFoundException, NotEnoughStockException {
        // Arrange
        int purchaseQuantity = 30;
        Sweet updatedSweet = Sweet.builder()
                .id(testSweetId)
                .name("Chocolate Bar")
                .category("Chocolate")
                .price(2.50)
                .quantity(70) // 100 - 30
                .build();
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(updatedSweet);

        // Act
        SweetSoldResponse result = sweetInventoryService.purchaseSweet(testSweetId, purchaseQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(70, result.getQuantity());
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(argThat(sweet -> 
            sweet.getQuantity() == 70 && 
            sweet.getId().equals(testSweetId)
        ));
    }

    @Test
    @DisplayName("restockSweet - Should verify quantity is updated correctly")
    void restockSweet_ShouldVerifyQuantityIsUpdatedCorrectly() throws SweetNotFoundException {
        // Arrange
        int restockQuantity = 25;
        Sweet updatedSweet = Sweet.builder()
                .id(testSweetId)
                .name("Chocolate Bar")
                .category("Chocolate")
                .price(2.50)
                .quantity(125) // 100 + 25
                .build();
        
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(updatedSweet);

        // Act
        Sweet result = sweetInventoryService.restockSweet(testSweetId, restockQuantity);

        // Assert
        assertNotNull(result);
        verify(sweetRepository, times(1)).findById(testSweetId);
        verify(sweetRepository, times(1)).save(argThat(sweet -> 
            sweet.getQuantity() == 125 && 
            sweet.getId().equals(testSweetId)
        ));
    }
}
