package com.quickdrop.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quickdrop.backend.model.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    // spring automatically writes the sql to find a transfer by its code
    Optional<Transfer> findByCode(String code);
}
