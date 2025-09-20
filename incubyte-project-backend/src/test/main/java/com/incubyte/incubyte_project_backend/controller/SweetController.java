package com.incubyte.incubyte_project_backend.controller;

import com.incubyte.incubyte_project_backend.dto.request.sweet.*;
import com.incubyte.incubyte_project_backend.dto.response.ApiResponse;
import com.incubyte.incubyte_project_backend.dto.response.sweet.SweetSoldResponse;
import com.incubyte.incubyte_project_backend.entity.Sweet;
import com.incubyte.incubyte_project_backend.exception.NotEnoughStockException;
import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.service.sweet.SweetCatalogService;
import com.incubyte.incubyte_project_backend.service.sweet.SweetInventoryService;
import com.incubyte.incubyte_project_backend.service.sweet.SweetManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing sweet products in the inventory system.
 * 
 * This controller provides comprehensive CRUD operations for sweet products,
 * including catalog browsing, inventory management, and sales operations.
 * It handles both public operations (viewing sweets) and admin-only operations
 * (managing inventory). All endpoints require authentication except where noted.
 * 
 * Base URL: /api/sweets
 * CORS: Enabled for http://localhost:3000 (frontend development)
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sweets")
@CrossOrigin("http://localhost:3000")
public class SweetController {
    
    /**
     * Service for catalog operations (viewing and searching sweets).
     */
    private final SweetCatalogService sweetCatalogService;
    
    /**
     * Service for sweet management operations (add, update, delete).
     * Requires ADMIN role for most operations.
     */
    private final SweetManagementService sweetManagementService;
    
    /**
     * Service for inventory operations (purchase, restock).
     */
    private final SweetInventoryService sweetInventoryService;

    /**
     * Retrieves all sweet products from the store.
     * 
     * This endpoint returns a list of all sweet products in the inventory,
     * regardless of their availability status. This includes both in-stock
     * and out-of-stock items.
     * 
     * @return ResponseEntity containing a list of all sweets
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully retrieved all sweets
     * - 401 Unauthorized: Authentication required
     */
    @GetMapping()
    ResponseEntity<ApiResponse> getAllSweet() {
        List<Sweet> sweets = sweetCatalogService.getAllSweetOfStore();
        return ResponseEntity.ok(new ApiResponse("Sweets retrieved successfully", sweets));
    }

    /**
     * Retrieves a specific sweet product by its unique identifier.
     * 
     * This endpoint returns detailed information about a single sweet product
     * identified by the provided UUID. If the sweet is not found, a 404 error
     * will be returned.
     * 
     * @param sweetId The unique identifier of the sweet to retrieve
     * @return ResponseEntity containing the sweet details
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully retrieved the sweet
     * - 404 Not Found: Sweet with the given ID does not exist
     * - 401 Unauthorized: Authentication required
     */
    @GetMapping("/{sweetId}")
    ResponseEntity<ApiResponse> getSweetById(@PathVariable UUID sweetId) {
        Sweet sweet = sweetCatalogService.getSweetById(sweetId);
        return ResponseEntity.ok(new ApiResponse("Sweet retrieved successfully", sweet));
    }

    /**
     * Retrieves all sweet products that are currently available (in stock).
     * 
     * This endpoint returns only sweet products that have a quantity greater
     * than zero, indicating they are available for purchase.
     * 
     * @return ResponseEntity containing a list of available sweets
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully retrieved available sweets
     * - 401 Unauthorized: Authentication required
     */
    @GetMapping("/available")
    ResponseEntity<ApiResponse> getAllAvailableSweet() {
        List<Sweet> sweets = sweetCatalogService.getAllAvailableSweet();
        return ResponseEntity.ok(new ApiResponse("Success", sweets));
    }

    /**
     * Retrieves all sweet products that are currently out of stock.
     * 
     * This endpoint returns only sweet products that have a quantity of zero,
     * indicating they are not available for purchase.
     * 
     * @return ResponseEntity containing a list of out-of-stock sweets
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully retrieved out-of-stock sweets
     * - 401 Unauthorized: Authentication required
     */
    @GetMapping("/not-available")
    ResponseEntity<ApiResponse> getNotAvailableSweet() {
        List<Sweet> sweets = sweetCatalogService.getNotAvailableSweet();
        return ResponseEntity.ok(new ApiResponse("Success", sweets));
    }


    /**
     * Searches for sweet products based on various criteria.
     * 
     * This endpoint allows searching for sweets using multiple optional filters:
     * name, category, minimum price, and maximum price. All search parameters
     * are optional and can be combined for more specific results.
     * 
     * @param searchSweetRequest The search criteria containing optional filters
     * @return ResponseEntity containing a list of matching sweets
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully retrieved search results
     * - 400 Bad Request: Validation errors in search criteria
     * - 401 Unauthorized: Authentication required
     * 
     * @see SearchSweetRequest
     */
    @GetMapping("/search")
    ResponseEntity<ApiResponse> searchSweet(@Valid SearchSweetRequest searchSweetRequest) {
        List<Sweet> sweets = sweetCatalogService.searchSweet(
                searchSweetRequest.getName(),
                searchSweetRequest.getCategory(),
                searchSweetRequest.getMinPrice(),
                searchSweetRequest.getMaxPrice()
        );

        return ResponseEntity.ok(new ApiResponse("Success", sweets));
    }

    /**
     * Adds a new sweet product to the inventory.
     * 
     * This endpoint creates a new sweet product in the system. The request
     * body is validated using Bean Validation annotations. This operation
     * requires ADMIN role authorization.
     * 
     * @param addSweetRequest The sweet details to be added
     * @return ResponseEntity containing the created sweet
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully added the sweet
     * - 400 Bad Request: Validation errors
     * - 401 Unauthorized: Authentication required
     * - 403 Forbidden: ADMIN role required
     * 
     * @see AddSweetRequest
     */
    @PostMapping()
    ResponseEntity<ApiResponse> addSweet(@Valid @RequestBody AddSweetRequest addSweetRequest) {
        Sweet requestSweet = Sweet.builder()
                .name(addSweetRequest.getName())
                .category(addSweetRequest.getCategory())
                .price(addSweetRequest.getPrice())
                .quantity(addSweetRequest.getQuantity())
                .build();

        Sweet newSweet = sweetManagementService.addSweet(requestSweet);

        return ResponseEntity.ok(new ApiResponse("Sweet Added Successfully", newSweet));
    }


    /**
     * Updates an existing sweet product in the inventory.
     * 
     * This endpoint allows partial updates of sweet product information.
     * All fields in the request are optional, allowing for selective updates.
     * This operation requires ADMIN role authorization.
     * 
     * @param sweetId The unique identifier of the sweet to update
     * @param updateSweetRequest The updated sweet details (partial update)
     * @return ResponseEntity containing the updated sweet
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully updated the sweet
     * - 400 Bad Request: Validation errors
     * - 401 Unauthorized: Authentication required
     * - 403 Forbidden: ADMIN role required
     * - 404 Not Found: Sweet with the given ID does not exist
     * 
     * @see UpdateSweetRequest
     * @see SweetNotFoundException
     */
    @PutMapping("/{sweetId}")
    ResponseEntity<ApiResponse> updateSweet(@PathVariable UUID sweetId, @Valid @RequestBody UpdateSweetRequest updateSweetRequest) {
        try {
            Sweet updatedSweet = sweetManagementService.updateSweet(sweetId, updateSweetRequest);

            return ResponseEntity.ok(new ApiResponse("Sweet Updated Successfully", updatedSweet));
        } catch (SweetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Purchases a specified quantity of a sweet product.
     * 
     * This endpoint handles the purchase of sweet products, reducing the
     * available quantity in stock. It validates that sufficient stock is
     * available before processing the purchase.
     * 
     * @param sweetId The unique identifier of the sweet to purchase
     * @param purchaseRequest The purchase details containing quantity
     * @return ResponseEntity containing purchase details and remaining stock
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully processed the purchase
     * - 400 Bad Request: Validation errors
     * - 401 Unauthorized: Authentication required
     * - 404 Not Found: Sweet with the given ID does not exist
     * - 409 Conflict: Insufficient stock available
     * 
     * @see PurchaseRequest
     * @see SweetSoldResponse
     * @see SweetNotFoundException
     * @see NotEnoughStockException
     */
    @PostMapping("/{sweetId}/purchase")
    ResponseEntity<ApiResponse> purchaseSweet(@PathVariable UUID sweetId, @Valid @RequestBody PurchaseRequest purchaseRequest) {
        try {
            SweetSoldResponse sweetSoldResponse = sweetInventoryService.purchaseSweet(sweetId, purchaseRequest.getQuantity());

            return ResponseEntity.ok(new ApiResponse("Sweet Purchased Successfully", sweetSoldResponse));
        } catch (SweetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (NotEnoughStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Restocks a sweet product by adding more quantity to the existing stock.
     * 
     * This endpoint adds the specified quantity to the existing stock of a
     * sweet product. This operation requires ADMIN role authorization.
     * 
     * @param sweetId The unique identifier of the sweet to restock
     * @param restockRequest The restock details containing quantity to add
     * @return ResponseEntity containing the updated sweet with new stock level
     * 
     * HTTP Status Codes:
     * - 200 OK: Successfully restocked the sweet
     * - 400 Bad Request: Validation errors
     * - 401 Unauthorized: Authentication required
     * - 403 Forbidden: ADMIN role required
     * - 404 Not Found: Sweet with the given ID does not exist
     * 
     * @see RestockRequest
     * @see SweetNotFoundException
     */
    @PostMapping("/{sweetId}/restock")
    ResponseEntity<ApiResponse> restockSweet(@PathVariable UUID sweetId, @Valid @RequestBody RestockRequest restockRequest) {
        try {
            Sweet restockSweet = sweetInventoryService.restockSweet(sweetId, restockRequest.getQuantity());

            return ResponseEntity.ok(new ApiResponse("Sweet Restocked Successfully", restockSweet));
        } catch (SweetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Deletes a sweet product from the inventory.
     * 
     * This endpoint permanently removes a sweet product from the system.
     * This operation requires ADMIN role authorization and cannot be undone.
     * 
     * @param sweetId The unique identifier of the sweet to delete
     * @return ResponseEntity with no content on successful deletion
     * 
     * HTTP Status Codes:
     * - 204 No Content: Successfully deleted the sweet
     * - 401 Unauthorized: Authentication required
     * - 403 Forbidden: ADMIN role required
     * - 404 Not Found: Sweet with the given ID does not exist
     * 
     * @see SweetNotFoundException
     */
    @DeleteMapping("/{sweetId}")
    ResponseEntity<ApiResponse> deleteSweet(@PathVariable UUID sweetId) {
        try {
            sweetManagementService.deleteSweet(sweetId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("Sweet Deleted Successfully", null));
        } catch (SweetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}