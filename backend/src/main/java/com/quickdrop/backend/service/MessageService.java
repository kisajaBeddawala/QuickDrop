package com.quickdrop.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.quickdrop.backend.model.Message;
import com.quickdrop.backend.model.Transfer;
import com.quickdrop.backend.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final TransferService transferService;

    public MessageService(MessageRepository messageRepository, TransferService transferService){
        this.messageRepository = messageRepository;
        this.transferService = transferService;
    }

    // method to add new messages to a transfer
    public Message addMessage(String transferCode, String content){
        // get the transfer using the transfer code
        Transfer transfer = transferService.getTransfer(transferCode);

        // create a new message instance
        Message message = new Message();

        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        message.setTransfer(transfer);

        return messageRepository.save(message);
    }

    // method to get all messages for a transfer
    public List<Message> getMessages(String transferCode){
        // get the transfer using the transfer code
        Transfer transfer = transferService.getTransfer(transferCode);
        // return all messages for that transfer
        return messageRepository.findByTransfer(transfer);
    }

    // method to delete message 
    public void deleteMessage(String transferCode, UUID messageId){
        Transfer transfer = transferService.getTransfer(transferCode);

        // find the message in database 
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));

        // make sure message belongs to this transfer
        if(!message.getTransfer().getId().equals(transfer.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Message does not belong to this transfer");
        }
        messageRepository.delete(message);
    }
}
