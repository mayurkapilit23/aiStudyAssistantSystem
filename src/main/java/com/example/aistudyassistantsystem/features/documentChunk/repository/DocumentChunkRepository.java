package com.example.aistudyassistantsystem.features.documentChunk.repository;

import com.example.aistudyassistantsystem.features.documentChunk.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentChunkRepository
        extends JpaRepository<DocumentChunk, Long> {

    List<DocumentChunk> findByDocument_IdOrderByChunkIndexAsc(
            Long documentId
    );

    boolean existsByDocument_Id(
            Long documentId
    );
}
