package com.quickdrop.backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// tells this class represents a database table
@Entity
// specifies the name of the table
@Table(name = "transfer")
// generates getters, setters, and toString methods for this class
@Data
public class Transfer {

    // field for primary key, auto-generated using UUID
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) private UUID id;

    // let us add database constraints. it can not be null and must be unique
    @Column(nullable = false, unique = true, length = 12) private String code;
    @Column(nullable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime expiresAt;
    @Column(nullable = false, length = 20) private String status;

    
}
