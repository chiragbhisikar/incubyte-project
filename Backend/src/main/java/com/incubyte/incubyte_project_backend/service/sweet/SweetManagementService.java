package com.incubyte.incubyte_project_backend.service.sweet;

import com.incubyte.incubyte_project_backend.dto.request.sweet.UpdateSweetRequest;
import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.entity.Sweet;

import java.util.UUID;

public interface SweetManagementService {
    Sweet addSweet(Sweet sweet);

    Sweet updateSweet(UUID sweetId, UpdateSweetRequest updateSweetRequest) throws SweetNotFoundException;

    void deleteSweet(UUID sweetId) throws SweetNotFoundException;
}
