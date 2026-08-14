package com.quickdrop.backend.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.quickdrop.backend.model.Transfer;
import com.quickdrop.backend.repository.TransferRepository;

import java.util.Random;

// tells the spring boot that this class holds our business logic
@Service
public class TransferService {
    // create one instance of this class for the entire application
    private final TransferRepository transferRepository;

    // constructor
    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    // method to create random code 
    private String generateRandomCode(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; i++){
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    public Transfer createTransfer(){
        Transfer transfer = new Transfer();
        transfer.setCode(generateRandomCode(6));
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setExpiresAt(LocalDateTime.now().plusHours(24));
        transfer.setStatus("ACTIVE");
        return transferRepository.save(transfer);
    }

    public Transfer getTransfer(String code){
        // find the transfer in the database
        Transfer transfer = transferRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found"));

        // check if the transfer is expired or not 
        if(transfer.getExpiresAt().isBefore(LocalDateTime.now())){
            transfer.setStatus("EXPIRED");
            transferRepository.save(transfer);
            throw new ResponseStatusException(HttpStatus.GONE, "Transfer expired");
        }

        return transfer;
    }
    
}
