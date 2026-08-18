package com.quickdrop.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickdrop.backend.dto.MessageRequest;
import com.quickdrop.backend.model.Message;
import com.quickdrop.backend.service.MessageService;

@RestController
@RequestMapping("/api/transfers/{code}/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    // use post method to add message
    @PostMapping
    public ResponseEntity<Message> addMessage(@PathVariable String code, @RequestBody MessageRequest request){

        Message newMessage = messageService.addMessage(code, request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(newMessage);
    }

    // use get method to get all messages
    @GetMapping
    public ResponseEntity<List<Message>> getMessages(@PathVariable String code){
        // get all messages for the transfer
        List<Message> messages = messageService.getMessages(code);

        // return the messages
        return ResponseEntity.ok(messages);
    }

    // use delete method to delete a message
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String code, @PathVariable UUID messageId){
        // delete message using the message service
        messageService.deleteMessage(code, messageId);
        
        // return 204 no content status
        return ResponseEntity.noContent().build();
    }
}
