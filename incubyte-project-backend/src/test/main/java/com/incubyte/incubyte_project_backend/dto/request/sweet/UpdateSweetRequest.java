package com.incubyte.incubyte_project_backend.dto.request.sweet;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for updating existing sweet products in the inventory.
 * 
 * This DTO encapsulates the data that can be updated for an existing sweet product.
 * All fields are optional, allowing for partial updates. Each field has appropriate
 * validation rules to ensure data quality and business rule compliance.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
public class UpdateSweetRequest {
    
    /**
     * Updated name of the sweet product.
     * 
     * Validation rules:
     * - Can only contain letters and spaces
     * - Must be between 2 and 50 characters long
     * - Optional field (can be null for no update)
     * 
     * @see jakarta.validation.constraints.Pattern
     * @see jakarta.validation.constraints.Size
     */
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters and spaces")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    /**
     * Updated category of the sweet product.
     * 
     * Validation rules:
     * - Cannot contain numbers
     * - Must be between 2 and 30 characters long
     * - Optional field (can be null for no update)
     * 
     * @see jakarta.validation.constraints.Pattern
     * @see jakarta.validation.constraints.Size
     */
    @Pattern(regexp = "^[^0-9]*$", message = "Category must not contain numbers")
    @Size(min = 2, max = 30, message = "Category must be between 2 and 30 characters")
    private String category;

    /**
     * Updated price of the sweet product.
     * 
     * Validation rules:
     * - Must be greater than 0.01 (minimum price)
     * - Optional field (can be null for no update)
     * 
     * @see jakarta.validation.constraints.DecimalMin
     */
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;

    /**
     * Updated quantity of the sweet product in stock.
     * 
     * Validation rules:
     * - Must be 0 or positive (cannot be negative)
     * - Optional field (can be null for no update)
     * 
     * @see jakarta.validation.constraints.Min
     */
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}