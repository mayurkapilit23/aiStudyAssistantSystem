package com.example.aistudyassistantsystem.features.documentChunk.service;

import com.example.aistudyassistantsystem.core.exception.ResourceNotFoundException;
import com.example.aistudyassistantsystem.features.document.entity.Document;
import com.example.aistudyassistantsystem.features.document.repository.DocumentRepository;
import com.example.aistudyassistantsystem.features.documentChunk.dto.DocumentChunkResponse;
import com.example.aistudyassistantsystem.features.documentChunk.entity.DocumentChunk;
import com.example.aistudyassistantsystem.features.documentChunk.repository.DocumentChunkRepository;
import com.example.aistudyassistantsystem.features.documentContent.entity.DocumentContent;
import com.example.aistudyassistantsystem.features.documentContent.repository.DocumentContentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentChunkServiceImpl
        implements DocumentChunkService {

    private final DocumentRepository
            documentRepository;

    private final DocumentContentRepository
            documentContentRepository;

    private final DocumentChunkRepository
            documentChunkRepository;

    private static final int CHUNK_SIZE = 1000;

    @Transactional
    @Override
    public List<DocumentChunkResponse> chunkDocument(
            Long documentId
    ) {

        // Prevent duplicate chunking
        boolean alreadyChunked =
                documentChunkRepository
                        .existsByDocument_Id(documentId);

        if (alreadyChunked) {

            return getChunks(documentId);
        }

        // Find document
        Document document =
                documentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found"
                                )
                        );

        // Get extracted text
        DocumentContent documentContent =
                documentContentRepository
                        .findByDocument_Id(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document content not found"
                                )
                        );

        String text =
                documentContent.getExtractedText();

        List<DocumentChunk> chunks =
                new ArrayList<>();

        int chunkIndex = 0;

        for (int i = 0;
             i < text.length();
             i += CHUNK_SIZE) {

            String chunkText =
                    text.substring(
                            i,
                            Math.min(
                                    i + CHUNK_SIZE,
                                    text.length()
                            )
                    );

            DocumentChunk chunk =
                    DocumentChunk.builder()
                            .chunkIndex(chunkIndex++)
                            .chunkText(chunkText)
                            .document(document)
                            .build();

            chunks.add(chunk);
        }

        List<DocumentChunk> savedChunks =
                documentChunkRepository.saveAll(chunks);

        return savedChunks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional()
    @Override
    public List<DocumentChunkResponse> getChunks(
            Long documentId
    ) {

        List<DocumentChunk> chunks =
                documentChunkRepository
                        .findByDocument_IdOrderByChunkIndexAsc(
                                documentId
                        );

        return chunks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DocumentChunkResponse mapToResponse(
            DocumentChunk chunk
    ) {

        return DocumentChunkResponse.builder()
                .id(chunk.getId())
                .chunkIndex(chunk.getChunkIndex())
                .chunkText(chunk.getChunkText())
                .build();
    }
}
