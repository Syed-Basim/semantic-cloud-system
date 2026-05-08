package com.example.rest_api_build.service;

import com.example.rest_api_build.entity.FileDocument;
import com.example.rest_api_build.entity.User;
import com.example.rest_api_build.repository.FileDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

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
}