package com.example.rest_api_build.repository;

import com.example.rest_api_build.entity.FileDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDocumentRepository
        extends JpaRepository<FileDocument, Long> {
}