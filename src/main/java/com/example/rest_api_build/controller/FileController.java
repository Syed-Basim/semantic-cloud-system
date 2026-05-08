package com.example.rest_api_build.controller;

import com.example.rest_api_build.entity.FileDocument;
import com.example.rest_api_build.entity.User;
import com.example.rest_api_build.repository.UserRepository;
import com.example.rest_api_build.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.rest_api_build.dto.FileResponseDTO;
import java.io.IOException;

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
}