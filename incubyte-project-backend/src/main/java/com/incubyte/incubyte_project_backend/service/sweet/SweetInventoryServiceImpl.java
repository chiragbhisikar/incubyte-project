package com.incubyte.incubyte_project_backend.service.sweet;

import com.incubyte.incubyte_project_backend.dto.response.sweet.SweetSoldResponse;
import com.incubyte.incubyte_project_backend.exception.NotEnoughStockException;
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
public class SweetInventoryServiceImpl implements SweetInventoryService {
    private final SweetRepository sweetRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public SweetSoldResponse purchaseSweet(UUID sweetId, int quantity) throws SweetNotFoundException, NotEnoughStockException {
        Sweet sweet = sweetRepository.findById(sweetId).orElseThrow(() -> new SweetNotFoundException("Sweet not found with id: " + sweetId));

        if (sweet.getQuantity() >= quantity) {
            int remainingQuantity = sweet.getQuantity() - quantity;
            Double totalAmount = quantity * sweet.getPrice();
            sweet.setQuantity(remainingQuantity);
            sweet = sweetRepository.save(sweet);

            return SweetSoldResponse.builder().sweet(sweet).quantity(remainingQuantity).totalAmount(Double.valueOf(String.format("%.2f", totalAmount))).build();
        }

        throw new NotEnoughStockException("Sweet Has Not Enough Quantity We Can Provide Only " + sweet.getQuantity() + " Quantities !");
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Sweet restockSweet(UUID sweetId, int quantity) throws SweetNotFoundException {
        Sweet sweet = sweetRepository.findById(sweetId).orElseThrow(() -> new SweetNotFoundException("Sweet not found with id: " + sweetId));

        int totalQuantity = sweet.getQuantity() + quantity;
        sweet.setQuantity(totalQuantity);

        return sweetRepository.save(sweet);
    }
}
