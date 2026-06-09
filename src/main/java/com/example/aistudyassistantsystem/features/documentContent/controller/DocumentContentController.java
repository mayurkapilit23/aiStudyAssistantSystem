package com.example.aistudyassistantsystem.features.documentContent.controller;

import com.example.aistudyassistantsystem.core.utils.dto.ApiResponse;
import com.example.aistudyassistantsystem.features.documentContent.dto.DocumentContentResponse;
import com.example.aistudyassistantsystem.features.documentContent.service.DocumentContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DocumentContentController {

    private final DocumentContentService documentContentService;

    @PostMapping("/document-contents/{documentId}/extract")
    public ResponseEntity<ApiResponse<DocumentContentResponse>> extractAndSave(@PathVariable Long documentId) {

        DocumentContentResponse data = documentContentService.extractAndSaveText(documentId);


        ApiResponse<DocumentContentResponse> response = ApiResponse.success("Document extracted and saved successfully", data);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/document-contents/{documentId}")
    public ResponseEntity<ApiResponse<DocumentContentResponse>> getContent(@PathVariable Long documentId) {

        DocumentContentResponse data = documentContentService.getContentByDocumentId(documentId);
        ApiResponse<DocumentContentResponse> response = ApiResponse.success("Document extracted content fetched successfully", data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
