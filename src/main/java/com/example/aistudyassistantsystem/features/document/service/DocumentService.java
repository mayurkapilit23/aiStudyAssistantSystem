package com.example.aistudyassistantsystem.features.document.service;

import com.example.aistudyassistantsystem.features.document.dto.DocumentUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentUploadResponseDto uploadFile(MultipartFile file);
    List<DocumentUploadResponseDto> getAllFiles();
    List<DocumentUploadResponseDto> uploadMultipleFiles(
            List<MultipartFile> files);

}





