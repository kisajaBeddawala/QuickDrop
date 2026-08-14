package com.quickdrop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.quickdrop.backend.model.Message;
import com.quickdrop.backend.model.Transfer;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    // automatically get all messages for a transfer
    List<Message> findByTransfer(Transfer transfer);
}
