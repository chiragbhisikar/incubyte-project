package com.incubyte.incubyte_project_backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sweet entity representing a sweet product in the inventory system.
 * 
 * This entity stores information about sweet products including their
 * name, category, price, and current quantity in stock. It also tracks
 * creation and modification timestamps for audit purposes.
 * 
 * Database table: sweets
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "sweets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sweet {
    
    /**
     * Unique identifier for the sweet.
     * Generated automatically using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Name of the sweet product.
     * Must not be null and maximum length is 50 characters.
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * Category of the sweet (e.g., Chocolate, Candy, Bakery).
     * Must not be null and maximum length is 50 characters.
     */
    @Column(nullable = false, length = 50)
    private String category;

    /**
     * Price of the sweet in the local currency.
     * Must not be null and must be greater than or equal to 0.0.
     */
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = true)
    private Double price;

    /**
     * Current quantity of the sweet in stock.
     * Must not be null and must be zero or positive.
     */
    @Column(nullable = false)
    @Min(value = 0, message = "Quantity must be zero or positive")
    private Integer quantity;

    /**
     * Timestamp when the sweet was first created.
     * Automatically set by Hibernate and cannot be updated.
     * Formatted as "yyyy-MM-dd HH:mm:ss" in JSON responses.
     */
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the sweet was last updated.
     * Automatically updated by Hibernate on every modification.
     * Formatted as "yyyy-MM-dd HH:mm:ss" in JSON responses.
     */
    @UpdateTimestamp
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}