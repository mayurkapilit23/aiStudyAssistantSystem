package com.example.aistudyassistantsystem.features.documentContent.service;

import com.example.aistudyassistantsystem.features.documentContent.dto.DocumentContentResponse;

public interface DocumentContentService {

    DocumentContentResponse extractAndSaveText(
            Long documentId
    );

    DocumentContentResponse getContentByDocumentId(
            Long documentId
    );
}
