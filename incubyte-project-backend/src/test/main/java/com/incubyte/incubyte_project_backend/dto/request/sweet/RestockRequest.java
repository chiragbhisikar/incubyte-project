package com.incubyte.incubyte_project_backend.dto.request.sweet;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object for sweet restock requests.
 * 
 * This DTO encapsulates the data required to restock a sweet product
 * in the inventory. It contains the quantity to be added to the existing
 * stock and includes validation to ensure the quantity is valid.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
public class RestockRequest {
    
    /**
     * Quantity of the sweet product to be added to the existing stock.
     * 
     * Validation rules:
     * - Cannot be null
     * - Must be at least 1 (minimum restock quantity)
     * 
     * @see jakarta.validation.constraints.NotNull
     * @see jakarta.validation.constraints.Min
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
