package com.incubyte.incubyte_project_backend.dto.response.sweet;

import com.incubyte.incubyte_project_backend.entity.Sweet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SweetSoldResponse {
    private Sweet sweet;
    private Double totalAmount;
    private Integer quantity;
}
