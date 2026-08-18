package com.quickdrop.backend.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quickdrop.backend.model.FileMetadata;
import com.quickdrop.backend.model.Transfer;
import com.quickdrop.backend.repository.FileMetadataRepository;


@Service
public class FileService {
    private final FileMetadataRepository fileMetadataRepository;
    private final TransferService transferService;
    private final Cloudinary cloudinary;

    public FileService(FileMetadataRepository fileMetadataRepository, TransferService transferService, @Value("${cloudinary.cloud-name}") String cloudName, @Value("${cloudinary.api-key}") String apiKey, @Value("${cloudinary.api-secret}") String apiSecret){
        this.fileMetadataRepository = fileMetadataRepository;
        this.transferService = transferService;

        // connect to cloudinary using secret credentials
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }

    public FileMetadata uploadFile(String transferCode, MultipartFile file) throws IOException{
        // Get transfer and check if exists
        Transfer transfer = transferService.getTransfer(transferCode);
        
        // upload the actual file bytes to cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "resource_type", "auto"
        ));

        // extract the public url that cloudinary generated
        String fileUrl = uploadResult.get("url").toString();
        String publicId = uploadResult.get("public_id").toString();
        String resourceType = uploadResult.get("resource_type").toString();

        // save file metadata to database
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setOriginalFileName(file.getOriginalFilename());
        fileMetadata.setSize(file.getSize());
        fileMetadata.setContentType(file.getContentType());
        fileMetadata.setCloudinaryPublicId(publicId);
        fileMetadata.setFileUrl(fileUrl);
        fileMetadata.setResourceType(resourceType);
        fileMetadata.setCreatedAt(LocalDateTime.now());
        fileMetadata.setTransfer(transfer);

        // return the saved file metadata
        return fileMetadataRepository.save(fileMetadata);
        
    }
    
    // method to get all files for a transfer
    public List<FileMetadata> getFiles(String transferCode){
        // Get transfer
        Transfer transfer = transferService.getTransfer(transferCode);
        
        // return all files for the transfer
        return fileMetadataRepository.findByTransfer(transfer);
    }
    
    // delete a file
    public void deleteFile(String transferCode, UUID fileId) throws IOException {
        // Get transfer
        Transfer transfer = transferService.getTransfer(transferCode);
        
        // Find the file
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found "));

        if (!fileMetadata.getTransfer().getId().equals(transfer.getId())) {
            throw new RuntimeException("File not found in this transfer");
        }

        // delete file from cloudinary
        cloudinary.uploader().destroy(fileMetadata.getCloudinaryPublicId(), ObjectUtils.asMap(
            "resource_type", fileMetadata.getResourceType()
        ));
        

        // delete file metadata from database
        fileMetadataRepository.delete(fileMetadata);
        
    }
    
}
