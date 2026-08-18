package com.quickdrop.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickdrop.backend.dto.JoinRequest;
import com.quickdrop.backend.model.Transfer;
import com.quickdrop.backend.service.TransferService;

// tell spring boot this handles HTTP requests related to transfers
@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    // create only one transfer service
    private final TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(){
        // call
        Transfer newTransfer = transferService.createTransfer();

        // wrap the result in a responseEntity
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransfer);
    }

    @PostMapping("/join")
    public ResponseEntity<Transfer> joinTransfer(@RequestBody JoinRequest request){
        Transfer transfer = transferService.getTransfer(request.getCode());
        return ResponseEntity.ok(transfer);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Transfer> getTransfer(@PathVariable String code){
        Transfer transfer = transferService.getTransfer(code);
        return ResponseEntity.ok(transfer);
    }


}
