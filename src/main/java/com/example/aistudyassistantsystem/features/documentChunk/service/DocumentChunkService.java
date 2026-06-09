package com.example.aistudyassistantsystem.features.documentChunk.service;

import com.example.aistudyassistantsystem.features.documentChunk.dto.DocumentChunkResponse;

import java.util.List;

public interface DocumentChunkService {

    List<DocumentChunkResponse> chunkDocument(
            Long documentId
    );

    List<DocumentChunkResponse> getChunks(
            Long documentId
    );
}
