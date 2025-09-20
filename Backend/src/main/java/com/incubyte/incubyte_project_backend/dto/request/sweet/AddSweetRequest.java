package com.incubyte.incubyte_project_backend.dto.request.sweet;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Data Transfer Object for adding new sweet products to the inventory.
 * 
 * This DTO encapsulates the data required to create a new sweet product
 * in the system. It includes comprehensive validation rules to ensure
 * data quality and business rule compliance. All fields are validated
 * using Bean Validation annotations.
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
public class AddSweetRequest {
    
    /**
     * Name of the sweet product.
     * 
     * Validation rules:
     * - Cannot be null, blank, or whitespace only
     * - Must be between 3 and 50 characters long
     * - Can only contain letters, spaces, hyphens, apostrophes, and ampersands
     * - Cannot contain numbers or other special characters
     * 
     * @see jakarta.validation.constraints.NotBlank
     * @see jakarta.validation.constraints.Pattern
     * @see org.hibernate.validator.constraints.Length
     */
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z\\s\\-'&]+$", message = "Name must not contain numbers or special characters")
    @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    /**
     * Category of the sweet product (e.g., Chocolate, Candy, Bakery).
     * 
     * Validation rules:
     * - Cannot be null, blank, or whitespace only
     * - Must be between 2 and 50 characters long
     * - Can only contain letters, spaces, hyphens, apostrophes, and ampersands
     * - Cannot contain numbers or other special characters
     * 
     * @see jakarta.validation.constraints.NotBlank
     * @see jakarta.validation.constraints.Pattern
     * @see org.hibernate.validator.constraints.Length
     */
    @NotBlank(message = "Category is required")
    @Pattern(regexp = "^[a-zA-Z\\s\\-'&]+$", message = "Category must not contain numbers or special characters")
    @Length(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;

    /**
     * Price of the sweet product in the local currency.
     * 
     * Validation rules:
     * - Cannot be null
     * - Must be greater than 0 (positive value)
     * 
     * @see jakarta.validation.constraints.NotNull
     * @see jakarta.validation.constraints.Positive
     */
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    /**
     * Initial quantity of the sweet product in stock.
     * 
     * Validation rules:
     * - Cannot be null
     * - Must be at least 1 (minimum stock quantity)
     * 
     * @see jakarta.validation.constraints.NotNull
     * @see jakarta.validation.constraints.Min
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

