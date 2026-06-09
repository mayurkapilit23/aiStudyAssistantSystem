package com.example.aistudyassistantsystem.features.documentChunk.controller;

import com.example.aistudyassistantsystem.core.utils.dto.ApiResponse;
import com.example.aistudyassistantsystem.features.documentChunk.dto.DocumentChunkResponse;
import com.example.aistudyassistantsystem.features.documentChunk.service.DocumentChunkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DocumentChunkController {

    private final DocumentChunkService documentChunkService;

    @PostMapping("/document-chunks/{documentId}")
    public ResponseEntity<ApiResponse<List<DocumentChunkResponse>>> chunkDocument(@PathVariable Long documentId) {

        List<DocumentChunkResponse> data = documentChunkService.chunkDocument(documentId);
        ApiResponse<List<DocumentChunkResponse>> response = ApiResponse.success("Document chunked successfully", data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/document-chunks/{documentId}")
    public ResponseEntity<ApiResponse<List<DocumentChunkResponse>>> getChunks(@PathVariable Long documentId) {

        List<DocumentChunkResponse> data = documentChunkService.getChunks(documentId);
        ApiResponse<List<DocumentChunkResponse>> response = ApiResponse.success("Chunks fetched successfully", data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
