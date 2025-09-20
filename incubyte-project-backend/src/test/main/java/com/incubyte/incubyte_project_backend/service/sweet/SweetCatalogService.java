package com.incubyte.incubyte_project_backend.service.sweet;

import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.entity.Sweet;
import java.util.List;
import java.util.UUID;

public interface SweetCatalogService {
    List<Sweet> getAllSweetOfStore();

    Sweet getSweetById(UUID sweetId) throws SweetNotFoundException;

    List<Sweet> getAllAvailableSweet();

    List<Sweet> getNotAvailableSweet();

    List<Sweet> searchSweet(String name, String category, Double minPrice, Double maxPrice);
}
