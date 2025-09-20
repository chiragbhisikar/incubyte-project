package com.incubyte.incubyte_project_backend.service.sweet;

import com.incubyte.incubyte_project_backend.dto.response.sweet.SweetSoldResponse;
import com.incubyte.incubyte_project_backend.exception.NotEnoughStockException;
import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.entity.Sweet;

import java.util.UUID;

public interface SweetInventoryService {
    SweetSoldResponse purchaseSweet(UUID sweetId, int quantity) throws SweetNotFoundException, NotEnoughStockException;

    Sweet restockSweet(UUID sweetId, int quantity) throws SweetNotFoundException;
}