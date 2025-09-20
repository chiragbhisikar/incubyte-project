package com.incubyte.incubyte_project_backend.repository.sweet;

import com.incubyte.incubyte_project_backend.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SweetRepository extends JpaRepository<Sweet, UUID> {
}