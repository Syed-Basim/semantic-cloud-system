package com.example.rest_api_build.controller;

import com.example.rest_api_build.dto.FileResponseDTO;
import com.example.rest_api_build.entity.FileDocument;
import com.example.rest_api_build.entity.User;
import com.example.rest_api_build.repository.UserRepository;
import com.example.rest_api_build.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<FileResponseDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws IOException {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        FileDocument savedFile =
                fileService.uploadFile(file, user);

        FileResponseDTO response =
                new FileResponseDTO(
                        savedFile.getId(),
                        savedFile.getFileName(),
                        savedFile.getFileType(),
                        savedFile.getFileSize(),
                        savedFile.getFilePath(),
                        savedFile.getUploadedAt(),
                        savedFile.getUser().getEmail()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-files")
    public ResponseEntity<List<FileResponseDTO>> getMyFiles(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<FileResponseDTO> files =
                fileService.getUserFiles(user);

        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long id,
            Authentication authentication
    ) throws IOException {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        FileDocument fileDocument =
                fileService.getFileByIdAndUser(id, user);

        Path path = Paths.get(fileDocument.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                fileDocument.getFileName() + "\""
                )
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long id,
            Authentication authentication
    ) throws IOException {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        fileService.deleteFile(id, user);

        return ResponseEntity.ok("File deleted successfully");
    }

    @GetMapping("/search-files")
    public ResponseEntity<List<FileResponseDTO>> searchFiles(
            @RequestParam String keyword,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<FileResponseDTO> files =
                fileService.searchFiles(keyword, user);

        return ResponseEntity.ok(files);
    }

}