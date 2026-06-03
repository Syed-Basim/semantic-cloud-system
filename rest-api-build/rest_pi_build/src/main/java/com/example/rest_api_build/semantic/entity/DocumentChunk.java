package com.example.rest_api_build.semantic.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fileId;

    private Integer chunkIndex;

    @Column(columnDefinition = "TEXT")
    private String chunkText;

    @Column(columnDefinition = "vector(384)")
    private String embedding;

    public DocumentChunk() {
    }

    public DocumentChunk(Long fileId,
                         Integer chunkIndex,
                         String chunkText,
                         String embedding) {
        this.fileId = fileId;
        this.chunkIndex = chunkIndex;
        this.chunkText = chunkText;
        this.embedding = embedding;
    }

    public Long getId() {
        return id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public String getChunkText() {
        return chunkText;
    }

    public void setChunkText(String chunkText) {
        this.chunkText = chunkText;
    }

    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(String embedding) {
        this.embedding = embedding;
    }
}