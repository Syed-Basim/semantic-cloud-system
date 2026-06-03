package com.example.rest_api_build.semantic.repository;

import com.example.rest_api_build.semantic.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentChunkRepository
        extends JpaRepository<DocumentChunk, Long> {
}