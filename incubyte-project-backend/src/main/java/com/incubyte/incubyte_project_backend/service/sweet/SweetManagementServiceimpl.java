package com.incubyte.incubyte_project_backend.service.sweet;

import com.incubyte.incubyte_project_backend.dto.request.sweet.UpdateSweetRequest;
import com.incubyte.incubyte_project_backend.exception.SweetNotFoundException;
import com.incubyte.incubyte_project_backend.entity.Sweet;
import com.incubyte.incubyte_project_backend.repository.sweet.SweetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SweetManagementServiceimpl implements SweetManagementService {
    private final SweetRepository sweetRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Sweet addSweet(Sweet sweet) {
        return sweetRepository.save(sweet);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Sweet updateSweet(UUID sweetId, UpdateSweetRequest updateSweetRequest) throws SweetNotFoundException {
        Sweet oldSweet = sweetRepository.findById(sweetId).orElseThrow(() -> new SweetNotFoundException("Sweet not found with id: " + sweetId));

        if(updateSweetRequest == null) return oldSweet;
        if (updateSweetRequest.getName() != null) {
            oldSweet.setName(updateSweetRequest.getName());
        }

        if (updateSweetRequest.getCategory() != null) {
            oldSweet.setCategory(updateSweetRequest.getCategory());
        }

        if (updateSweetRequest.getQuantity() != null) {
            oldSweet.setQuantity(updateSweetRequest.getQuantity());
        }

        if (updateSweetRequest.getPrice() != null) {
            oldSweet.setPrice(updateSweetRequest.getPrice());
        }

        return sweetRepository.save(oldSweet);
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSweet(UUID sweetId) throws SweetNotFoundException {
        Sweet sweet = sweetRepository.findById(sweetId).orElseThrow(() -> new SweetNotFoundException("Sweet not found with id: " + sweetId));
        sweetRepository.delete(sweet);
    }
}
