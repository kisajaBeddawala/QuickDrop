package com.quickdrop.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quickdrop.backend.model.FileMetadata;
import com.quickdrop.backend.model.Transfer;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata,UUID>{
    // automatically gets all files for a transfer
    List<FileMetadata> findByTransfer(Transfer transfer);
}
