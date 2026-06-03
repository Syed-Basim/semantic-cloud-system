package com.example.rest_api_build.service;

import com.example.rest_api_build.dto.FileResponseDTO;
import com.example.rest_api_build.entity.FileDocument;
import com.example.rest_api_build.entity.User;
import com.example.rest_api_build.repository.FileDocumentRepository;

import com.example.rest_api_build.exception.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.time.LocalDateTime;

import java.util.UUID;

@Service
public class FileService {

    private final String STORAGE_FOLDER = "storage/";

    @Autowired
    private FileDocumentRepository fileRepository;

    public FileDocument uploadFile(
            MultipartFile file,
            User user
    ) throws IOException {

        String originalFileName =
                file.getOriginalFilename();

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

        FileDocument fileDocument =
                new FileDocument();

        fileDocument.setFileName(originalFileName);
        fileDocument.setFileType(file.getContentType());
        fileDocument.setFilePath(filePath.toString());
        fileDocument.setFileSize(file.getSize());
        fileDocument.setUploadedAt(LocalDateTime.now());
        fileDocument.setUser(user);

        return fileRepository.save(fileDocument);
    }

    public Page<FileResponseDTO> getUserFiles(
            User user,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {

        Sort sort =
                sortDirection.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<FileDocument> files =
                fileRepository.findByUser(user, pageable);

        return files.map(this::mapToResponseDTO);
    }

    public FileResponseDTO mapToResponseDTO(
            FileDocument file
    ) {

        FileResponseDTO dto =
                new FileResponseDTO();

        dto.setId(file.getId());
        dto.setFileName(file.getFileName());
        dto.setFileType(file.getFileType());
        dto.setFileSize(file.getFileSize());
        dto.setFilePath(file.getFilePath());
        dto.setUploadedAt(file.getUploadedAt());

        if (file.getUser() != null) {
            dto.setUploadedBy(
                    file.getUser().getEmail()
            );
        }

        return dto;
    }

    public FileDocument getFileByIdAndUser(
            Long fileId,
            User user
    ) {

        return fileRepository
                .findByIdAndUser(fileId, user)
                .orElseThrow(() ->
                        new FileNotFoundException(
                                "File not found"
                        ));
    }

    public void deleteFile(
            Long fileId,
            User user
    ) throws IOException {

        FileDocument fileDocument =
                fileRepository
                        .findByIdAndUser(fileId, user)
                        .orElseThrow(() ->
                                new FileNotFoundException(
                                        "File not found"
                                ));

        Path path =
                Paths.get(fileDocument.getFilePath());

        Files.deleteIfExists(path);

        fileRepository.delete(fileDocument);
    }

    public Page<FileResponseDTO> searchFiles(
            String keyword,
            User user,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {

        Sort sort =
                sortDirection.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<FileDocument> files =
                fileRepository
                        .findByUserAndFileNameContainingIgnoreCase(
                                user,
                                keyword,
                                pageable
                        );

        return files.map(this::mapToResponseDTO);
    }
}