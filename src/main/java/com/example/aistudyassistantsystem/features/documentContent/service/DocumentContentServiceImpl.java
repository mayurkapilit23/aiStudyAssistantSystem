package com.example.aistudyassistantsystem.features.documentContent.service;

import com.example.aistudyassistantsystem.core.exception.ResourceNotFoundException;
import com.example.aistudyassistantsystem.core.utils.services.FileTextExtractorService;
import com.example.aistudyassistantsystem.features.documentContent.dto.DocumentContentResponse;
import com.example.aistudyassistantsystem.features.document.entity.Document;
import com.example.aistudyassistantsystem.features.documentContent.entity.DocumentContent;
import com.example.aistudyassistantsystem.features.documentContent.repository.DocumentContentRepository;
import com.example.aistudyassistantsystem.features.document.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentContentServiceImpl
        implements DocumentContentService {

    private final DocumentRepository
            documentRepository;

    private final DocumentContentRepository
            documentContentRepository;

    private final FileTextExtractorService
            fileTextExtractorService;

    @Transactional
    @Override
    public DocumentContentResponse extractAndSaveText(
            Long documentId
    ) {

        // Check existing extracted content
        DocumentContent existingContent =
                documentContentRepository
                        .findByDocument_Id(documentId)
                        .orElse(null);

        // Return existing data if already extracted
        if (existingContent != null) {

            return mapToResponse(existingContent);
        }

        Document document =
                documentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found"
                                )
                        );

        String extractedText =
                fileTextExtractorService
                        .extractText(documentId);

        DocumentContent documentContent =
                DocumentContent.builder()
                        .document(document)
                        .extractedText(extractedText)
                        .build();

        DocumentContent savedContent =
                documentContentRepository
                        .save(documentContent);

        return mapToResponse(savedContent);
    }

    @Transactional
    @Override
    public DocumentContentResponse getContentByDocumentId(
            Long documentId
    ) {

        DocumentContent content =
                documentContentRepository
                        .findByDocument_Id(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document content not found"
                                )
                        );

        return mapToResponse(content);
    }

    private DocumentContentResponse mapToResponse(
            DocumentContent content
    ) {

        return DocumentContentResponse.builder()
                .id(content.getId())
                .documentId(
                        content.getDocument().getId()
                )
                .extractedText(
                        content.getExtractedText()
                )
                .build();
    }
}
