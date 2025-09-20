package com.incubyte.incubyte_project_backend.dto.request.sweet;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class SearchSweetRequest {
    private String name;
    private String category;

    @PositiveOrZero(message = "Min price must be 0 or greater")
    private Double minPrice;

    @Positive(message = "Price must be greater than 0")
    private Double maxPrice;
}
