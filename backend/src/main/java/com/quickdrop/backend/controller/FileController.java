package com.quickdrop.backend.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quickdrop.backend.model.FileMetadata;
import com.quickdrop.backend.service.FileService;

@RestController
@RequestMapping("/api/transfers/{code}/files")
public class FileController {
    public final FileService fileService;

    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    // upload a file
    @PostMapping
    public ResponseEntity<?> uploadFile(@PathVariable String code, @RequestParam("file") MultipartFile file) {
        try {
            FileMetadata savedFile = fileService.uploadFile(code, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString() + " -> " + e.getMessage());
        }
    }

    // get files
    @GetMapping
    public ResponseEntity<List<FileMetadata>> getFiles(@PathVariable String code) {
        List<FileMetadata> files = fileService.getFiles(code);
        return ResponseEntity.ok(files);
    }

    // delete file
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String code, @PathVariable UUID fileId) throws IOException {
        fileService.deleteFile(code, fileId);
        return ResponseEntity.noContent().build();
    }
    
}
