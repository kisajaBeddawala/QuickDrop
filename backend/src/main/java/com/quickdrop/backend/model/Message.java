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
@Table(name = "messages")
@Data

public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) private UUID id;

    // establishes the relationship between this table and the transfer table
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "transfer_id", nullable = false) private Transfer transfer;

    @Column(nullable = false, columnDefinition = "TEXT") private String content;
    @Column(nullable = false) private LocalDateTime createdAt;

}
