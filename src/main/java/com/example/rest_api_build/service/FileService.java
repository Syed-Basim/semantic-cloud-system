package com.example.rest_api_build.service;

import com.example.rest_api_build.dto.FileResponseDTO;
import com.example.rest_api_build.entity.FileDocument;
import com.example.rest_api_build.entity.User;
import com.example.rest_api_build.repository.FileDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final String STORAGE_FOLDER = "storage/";

    @Autowired
    private FileDocumentRepository fileRepository;

    public FileDocument uploadFile(MultipartFile file, User user)
            throws IOException {

        String originalFileName = file.getOriginalFilename();

        String uniqueFileName =
                UUID.randomUUID() + "_" + originalFileName;

        Path filePath =
                Paths.get(STORAGE_FOLDER + uniqueFileName);

        Files.createDirectories(filePath.getParent());

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        FileDocument fileDocument = new FileDocument();

        fileDocument.setFileName(originalFileName);
        fileDocument.setFileType(file.getContentType());
        fileDocument.setFilePath(filePath.toString());
        fileDocument.setFileSize(file.getSize());
        fileDocument.setUploadedAt(LocalDateTime.now());
        fileDocument.setUser(user);

        return fileRepository.save(fileDocument);
    }

    public List<FileResponseDTO> getUserFiles(User user) {

        List<FileDocument> files =
                fileRepository.findByUser(user);

        return files.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private FileResponseDTO mapToResponseDTO(FileDocument file) {

        FileResponseDTO dto = new FileResponseDTO();

        dto.setId(file.getId());
        dto.setFileName(file.getFileName());
        dto.setFileType(file.getFileType());
        dto.setFileSize(file.getFileSize());
        dto.setFilePath(file.getFilePath());
        dto.setUploadedAt(file.getUploadedAt());

        if (file.getUser() != null) {
            dto.setUploadedBy(file.getUser().getEmail());
        }

        return dto;
    }

    public FileDocument getFileByIdAndUser(Long fileId, User user) {

        return fileRepository
                .findByIdAndUser(fileId, user)
                .orElseThrow(() ->
                        new RuntimeException("File not found"));
    }

    public void deleteFile(Long fileId, User user)
            throws IOException {

        FileDocument fileDocument =
                fileRepository
                        .findByIdAndUser(fileId, user)
                        .orElseThrow(() ->
                                new RuntimeException("File not found"));

        Path path = Paths.get(fileDocument.getFilePath());

        Files.deleteIfExists(path);

        fileRepository.delete(fileDocument);
    }

    public List<FileResponseDTO> searchFiles(
            String keyword,
            User user
    ) {

        List<FileDocument> files =
                fileRepository
                        .findByUserAndFileNameContainingIgnoreCase(
                                user,
                                keyword
                        );

        return files.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}