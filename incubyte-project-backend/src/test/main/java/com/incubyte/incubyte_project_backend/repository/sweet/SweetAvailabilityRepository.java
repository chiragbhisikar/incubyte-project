package com.incubyte.incubyte_project_backend.repository.sweet;

import com.incubyte.incubyte_project_backend.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SweetAvailabilityRepository extends JpaRepository<Sweet, UUID> {
    @Query("SELECT s FROM Sweet s WHERE s.quantity <= 0")
    List<Sweet> findOutOfStock();

    @Query("SELECT s FROM Sweet s WHERE s.quantity > 0")
    List<Sweet> findAvailable();
}