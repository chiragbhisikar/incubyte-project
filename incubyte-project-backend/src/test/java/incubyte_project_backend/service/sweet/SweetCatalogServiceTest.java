package incubyte_project_backend.service.sweet;

import com.incubyte.incubyte_project_backend.entity.Sweet;
import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetAvailabilityRepository;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetRepository;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetSearchRepository;
import com.incubyte.incubyte_project_backend.service.sweet.SweetCatalogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Sweet Catalog Service Tests")
class SweetCatalogServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @Mock
    private SweetAvailabilityRepository sweetAvailabilityRepository;

    @Mock
    private SweetSearchRepository sweetSearchRepository;

    @InjectMocks
    private SweetCatalogServiceImpl sweetCatalogService;

    private Sweet testSweet1;
    private Sweet testSweet2;
    private Sweet testSweet3;
    private UUID testSweetId;

    @BeforeEach
    void setUp() {
        testSweetId = UUID.randomUUID();
        
        testSweet1 = Sweet.builder()
                .id(testSweetId)
                .name("Chocolate Bar")
                .category("Chocolate")
                .price(2.50)
                .quantity(100)
                .build();

        testSweet2 = Sweet.builder()
                .id(UUID.randomUUID())
                .name("Gummy Bears")
                .category("Gummies")
                .price(1.75)
                .quantity(0)
                .build();

        testSweet3 = Sweet.builder()
                .id(UUID.randomUUID())
                .name("Lollipop")
                .category("Hard Candy")
                .price(0.75)
                .quantity(50)
                .build();
    }

    @Test
    @DisplayName("getAllSweetOfStore - Should return all sweets when repository has data")
    void getAllSweetOfStore_ShouldReturnAllSweets_WhenRepositoryHasData() {
        // Arrange
        List<Sweet> expectedSweets = Arrays.asList(testSweet1, testSweet2, testSweet3);
        when(sweetRepository.findAll()).thenReturn(expectedSweets);

        // Act
        List<Sweet> result = sweetCatalogService.getAllSweetOfStore();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(testSweet1));
        assertTrue(result.contains(testSweet2));
        assertTrue(result.contains(testSweet3));
        verify(sweetRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllSweetOfStore - Should return empty list when repository is empty")
    void getAllSweetOfStore_ShouldReturnEmptyList_WhenRepositoryIsEmpty() {
        // Arrange
        when(sweetRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Sweet> result = sweetCatalogService.getAllSweetOfStore();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sweetRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getSweetById - Should return sweet when valid ID is provided")
    void getSweetById_ShouldReturnSweet_WhenValidIdProvided() throws SweetNotFoundException {
        // Arrange
        when(sweetRepository.findById(testSweetId)).thenReturn(Optional.of(testSweet1));

        // Act
        Sweet result = sweetCatalogService.getSweetById(testSweetId);

        // Assert
        assertNotNull(result);
        assertEquals(testSweet1.getId(), result.getId());
        assertEquals(testSweet1.getName(), result.getName());
        assertEquals(testSweet1.getCategory(), result.getCategory());
        assertEquals(testSweet1.getPrice(), result.getPrice());
        assertEquals(testSweet1.getQuantity(), result.getQuantity());
        verify(sweetRepository, times(1)).findById(testSweetId);
    }

    @Test
    @DisplayName("getSweetById - Should throw SweetNotFoundException when invalid ID is provided")
    void getSweetById_ShouldThrowSweetNotFoundException_WhenInvalidIdProvided() {
        // Arrange
        UUID invalidId = UUID.randomUUID();
        when(sweetRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetCatalogService.getSweetById(invalidId);
        });

        assertEquals("Sweet not found with id: " + invalidId, exception.getMessage());
        verify(sweetRepository, times(1)).findById(invalidId);
    }

    @Test
    @DisplayName("getSweetById - Should throw SweetNotFoundException when null ID is provided")
    void getSweetById_ShouldThrowSweetNotFoundException_WhenNullIdProvided() {
        // Arrange
        when(sweetRepository.findById(null)).thenReturn(Optional.empty());

        // Act & Assert
        SweetNotFoundException exception = assertThrows(SweetNotFoundException.class, () -> {
            sweetCatalogService.getSweetById(null);
        });

        assertEquals("Sweet not found with id: null", exception.getMessage());
        verify(sweetRepository, times(1)).findById(null);
    }

    @Test
    @DisplayName("getAllAvailableSweet - Should return only available sweets")
    void getAllAvailableSweet_ShouldReturnOnlyAvailableSweets() {
        // Arrange
        List<Sweet> availableSweets = Arrays.asList(testSweet1, testSweet3);
        when(sweetAvailabilityRepository.findAvailable()).thenReturn(availableSweets);

        // Act
        List<Sweet> result = sweetCatalogService.getAllAvailableSweet();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testSweet1));
        assertTrue(result.contains(testSweet3));
        assertFalse(result.contains(testSweet2)); // This one has quantity 0
        verify(sweetAvailabilityRepository, times(1)).findAvailable();
    }

    @Test
    @DisplayName("getAllAvailableSweet - Should return empty list when no sweets are available")
    void getAllAvailableSweet_ShouldReturnEmptyList_WhenNoSweetsAvailable() {
        // Arrange
        when(sweetAvailabilityRepository.findAvailable()).thenReturn(Collections.emptyList());

        // Act
        List<Sweet> result = sweetCatalogService.getAllAvailableSweet();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sweetAvailabilityRepository, times(1)).findAvailable();
    }

    @Test
    @DisplayName("getNotAvailableSweet - Should return only out of stock sweets")
    void getNotAvailableSweet_ShouldReturnOnlyOutOfStockSweets() {
        // Arrange
        List<Sweet> outOfStockSweets = Arrays.asList(testSweet2);
        when(sweetAvailabilityRepository.findOutOfStock()).thenReturn(outOfStockSweets);

        // Act
        List<Sweet> result = sweetCatalogService.getNotAvailableSweet();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testSweet2));
        assertFalse(result.contains(testSweet1)); // This one has quantity > 0
        assertFalse(result.contains(testSweet3)); // This one has quantity > 0
        verify(sweetAvailabilityRepository, times(1)).findOutOfStock();
    }

    @Test
    @DisplayName("getNotAvailableSweet - Should return empty list when all sweets are available")
    void getNotAvailableSweet_ShouldReturnEmptyList_WhenAllSweetsAvailable() {
        // Arrange
        when(sweetAvailabilityRepository.findOutOfStock()).thenReturn(Collections.emptyList());

        // Act
        List<Sweet> result = sweetCatalogService.getNotAvailableSweet();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sweetAvailabilityRepository, times(1)).findOutOfStock();
    }

    @Test
    @DisplayName("searchSweet - Should return filtered sweets when valid search criteria provided")
    void searchSweet_ShouldReturnFilteredSweets_WhenValidSearchCriteriaProvided() {
        // Arrange
        String name = "Chocolate";
        String category = "Chocolate";
        Double minPrice = 1.0;
        Double maxPrice = 3.0;
        List<Sweet> searchResults = Arrays.asList(testSweet1);
        
        when(sweetSearchRepository.search(name, category, minPrice, maxPrice))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(name, category, minPrice, maxPrice);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testSweet1));
        verify(sweetSearchRepository, times(1)).search(name, category, minPrice, maxPrice);
    }

    @Test
    @DisplayName("searchSweet - Should return empty list when no sweets match search criteria")
    void searchSweet_ShouldReturnEmptyList_WhenNoSweetsMatchSearchCriteria() {
        // Arrange
        String name = "NonExistent";
        String category = "NonExistent";
        Double minPrice = 100.0;
        Double maxPrice = 200.0;
        
        when(sweetSearchRepository.search(name, category, minPrice, maxPrice))
                .thenReturn(Collections.emptyList());

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(name, category, minPrice, maxPrice);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sweetSearchRepository, times(1)).search(name, category, minPrice, maxPrice);
    }

    @Test
    @DisplayName("searchSweet - Should handle null search parameters")
    void searchSweet_ShouldHandleNullSearchParameters() {
        // Arrange
        List<Sweet> allSweets = Arrays.asList(testSweet1, testSweet2, testSweet3);
        when(sweetSearchRepository.search(null, null, null, null))
                .thenReturn(allSweets);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(null, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(sweetSearchRepository, times(1)).search(null, null, null, null);
    }

    @Test
    @DisplayName("searchSweet - Should handle partial search parameters")
    void searchSweet_ShouldHandlePartialSearchParameters() {
        // Arrange
        String name = "Chocolate";
        List<Sweet> searchResults = Arrays.asList(testSweet1);
        when(sweetSearchRepository.search(name, null, null, null))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(name, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testSweet1));
        verify(sweetSearchRepository, times(1)).search(name, null, null, null);
    }

    @Test
    @DisplayName("searchSweet - Should handle price range search")
    void searchSweet_ShouldHandlePriceRangeSearch() {
        // Arrange
        Double minPrice = 1.0;
        Double maxPrice = 2.0;
        List<Sweet> searchResults = Arrays.asList(testSweet1);
        when(sweetSearchRepository.search(null, null, minPrice, maxPrice))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(null, null, minPrice, maxPrice);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testSweet1));
        verify(sweetSearchRepository, times(1)).search(null, null, minPrice, maxPrice);
    }

    @Test
    @DisplayName("searchSweet - Should handle category search")
    void searchSweet_ShouldHandleCategorySearch() {
        // Arrange
        String category = "Chocolate";
        List<Sweet> searchResults = Arrays.asList(testSweet1);
        when(sweetSearchRepository.search(null, category, null, null))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(null, category, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testSweet1));
        verify(sweetSearchRepository, times(1)).search(null, category, null, null);
    }

    @Test
    @DisplayName("searchSweet - Should handle edge case with zero price range")
    void searchSweet_ShouldHandleEdgeCaseWithZeroPriceRange() {
        // Arrange
        Double minPrice = 0.0;
        Double maxPrice = 0.0;
        List<Sweet> searchResults = Collections.emptyList();
        when(sweetSearchRepository.search(null, null, minPrice, maxPrice))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(null, null, minPrice, maxPrice);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sweetSearchRepository, times(1)).search(null, null, minPrice, maxPrice);
    }

    @Test
    @DisplayName("searchSweet - Should handle edge case with negative price range")
    void searchSweet_ShouldHandleEdgeCaseWithNegativePriceRange() {
        // Arrange
        Double minPrice = -1.0;
        Double maxPrice = -0.5;
        List<Sweet> searchResults = Collections.emptyList();
        when(sweetSearchRepository.search(null, null, minPrice, maxPrice))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(null, null, minPrice, maxPrice);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sweetSearchRepository, times(1)).search(null, null, minPrice, maxPrice);
    }

    @Test
    @DisplayName("searchSweet - Should handle edge case with very large price range")
    void searchSweet_ShouldHandleEdgeCaseWithVeryLargePriceRange() {
        // Arrange
        Double minPrice = 0.0;
        Double maxPrice = Double.MAX_VALUE;
        List<Sweet> searchResults = Arrays.asList(testSweet1, testSweet2, testSweet3);
        when(sweetSearchRepository.search(null, null, minPrice, maxPrice))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(null, null, minPrice, maxPrice);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(sweetSearchRepository, times(1)).search(null, null, minPrice, maxPrice);
    }

    @Test
    @DisplayName("searchSweet - Should handle empty string search parameters")
    void searchSweet_ShouldHandleEmptyStringSearchParameters() {
        // Arrange
        String name = "";
        String category = "";
        List<Sweet> searchResults = Arrays.asList(testSweet1, testSweet2, testSweet3);
        when(sweetSearchRepository.search(name, category, null, null))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(name, category, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(sweetSearchRepository, times(1)).search(name, category, null, null);
    }

    @Test
    @DisplayName("searchSweet - Should handle whitespace in search parameters")
    void searchSweet_ShouldHandleWhitespaceInSearchParameters() {
        // Arrange
        String name = "  Chocolate  ";
        String category = "  Chocolate  ";
        List<Sweet> searchResults = Arrays.asList(testSweet1);
        when(sweetSearchRepository.search(name, category, null, null))
                .thenReturn(searchResults);

        // Act
        List<Sweet> result = sweetCatalogService.searchSweet(name, category, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(testSweet1));
        verify(sweetSearchRepository, times(1)).search(name, category, null, null);
    }

    @Test
    @DisplayName("getAllSweetOfStore - Should handle repository exception")
    void getAllSweetOfStore_ShouldHandleRepositoryException() {
        // Arrange
        when(sweetRepository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetCatalogService.getAllSweetOfStore();
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getSweetById - Should handle repository exception")
    void getSweetById_ShouldHandleRepositoryException() {
        // Arrange
        when(sweetRepository.findById(testSweetId)).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetCatalogService.getSweetById(testSweetId);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetRepository, times(1)).findById(testSweetId);
    }

    @Test
    @DisplayName("getAllAvailableSweet - Should handle repository exception")
    void getAllAvailableSweet_ShouldHandleRepositoryException() {
        // Arrange
        when(sweetAvailabilityRepository.findAvailable()).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetCatalogService.getAllAvailableSweet();
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetAvailabilityRepository, times(1)).findAvailable();
    }

    @Test
    @DisplayName("getNotAvailableSweet - Should handle repository exception")
    void getNotAvailableSweet_ShouldHandleRepositoryException() {
        // Arrange
        when(sweetAvailabilityRepository.findOutOfStock()).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetCatalogService.getNotAvailableSweet();
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetAvailabilityRepository, times(1)).findOutOfStock();
    }

    @Test
    @DisplayName("searchSweet - Should handle repository exception")
    void searchSweet_ShouldHandleRepositoryException() {
        // Arrange
        when(sweetSearchRepository.search(anyString(), anyString(), any(), any()))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sweetCatalogService.searchSweet("test", "test", 1.0, 10.0);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(sweetSearchRepository, times(1)).search("test", "test", 1.0, 10.0);
    }
}
