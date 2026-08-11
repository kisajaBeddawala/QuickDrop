package com.quickdrop.backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "files")
@Data
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) private UUID id;

    // many files can belong to one transfer
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "transfer_id", nullable = false) private Transfer transfer;

    @Column(nullable = false) private String originalFileName;
    @Column(nullable = false) private String cloudinaryPublicId;
    @Column(nullable = false) private String resourceType;
    @Column private String contentType;
    @Column(nullable = false) private Long size;
    @Column(nullable = false) private LocalDateTime createdAt;

    
}
