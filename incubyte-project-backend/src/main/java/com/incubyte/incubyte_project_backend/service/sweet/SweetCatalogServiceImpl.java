package com.incubyte.incubyte_project_backend.service.sweet;


import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.entity.Sweet;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetAvailabilityRepository;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetRepository;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SweetCatalogServiceImpl implements SweetCatalogService {
    private final SweetRepository sweetRepository;
    private final SweetAvailabilityRepository sweetAvailabilityRepository;
    private final SweetSearchRepository sweetSearchRepository;

    public List<Sweet> getAllSweetOfStore() {
        return sweetRepository.findAll();
    }

    public Sweet getSweetById(UUID sweetId) throws SweetNotFoundException {
        return sweetRepository.findById(sweetId).orElseThrow(() -> new SweetNotFoundException("Sweet not found with id: " + sweetId));
    }

    public List<Sweet> getAllAvailableSweet() {
        return sweetAvailabilityRepository.findAvailable();
    }

    @Override
    public List<Sweet> getNotAvailableSweet() {
        return sweetAvailabilityRepository.findOutOfStock();
    }

    @Override
    public List<Sweet> searchSweet(String name, String category, Double minPrice, Double maxPrice) {
        return sweetSearchRepository.search(name, category, minPrice, maxPrice);
    }
}
