package com.example.rest_api_build.repository;

import com.example.rest_api_build.entity.FileDocument;
import com.example.rest_api_build.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface FileDocumentRepository
        extends JpaRepository<FileDocument, Long> {

    List<FileDocument> findByUser(User user);

    Optional<FileDocument> findByIdAndUser(Long id, User user);

    List<FileDocument>
    findByUserAndFileNameContainingIgnoreCase(
            User user,
            String keyword
    );
}