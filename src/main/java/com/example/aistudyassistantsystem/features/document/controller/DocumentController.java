package com.example.aistudyassistantsystem.features.document.controller;

import com.example.aistudyassistantsystem.core.utils.dto.ApiResponse;
import com.example.aistudyassistantsystem.features.document.dto.DocumentUploadResponseDto;
import com.example.aistudyassistantsystem.features.documentContent.service.DocumentContentService;
import com.example.aistudyassistantsystem.features.document.service.DocumentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentContentService documentContentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/files")
    public ResponseEntity<ApiResponse<DocumentUploadResponseDto>> uploadFile(@RequestParam("file") @NotNull(message = "File is required") MultipartFile file) {

        DocumentUploadResponseDto data = documentService.uploadFile(file);
        ApiResponse<DocumentUploadResponseDto> response = ApiResponse.success("File uploaded successfully", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/files")
    public ResponseEntity<ApiResponse<List<DocumentUploadResponseDto>>> getAllFiles() {
        List<DocumentUploadResponseDto> data = documentService.getAllFiles();
        ApiResponse<List<DocumentUploadResponseDto>> response = ApiResponse.success("File fetched successfully", data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/files/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<DocumentUploadResponseDto>>> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {

        List<DocumentUploadResponseDto> data = documentService.uploadMultipleFiles(files);

        ApiResponse<List<DocumentUploadResponseDto>> response = ApiResponse.success("File uploaded successfully", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
