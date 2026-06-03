package com.example.rest_api_build.repository;

import com.example.rest_api_build.entity.FileDocument;
import com.example.rest_api_build.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FileDocumentRepository
        extends JpaRepository<FileDocument, Long> {

    Page<FileDocument> findByUser(
            User user,
            Pageable pageable
    );

    Optional<FileDocument> findByIdAndUser(
            Long id,
            User user
    );

    Page<FileDocument>
    findByUserAndFileNameContainingIgnoreCase(
            User user,
            String keyword,
            Pageable pageable
    );
}
