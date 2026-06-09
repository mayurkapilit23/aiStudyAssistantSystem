package com.example.aistudyassistantsystem.features.document.service;

import com.example.aistudyassistantsystem.core.exception.FileUploadException;
import com.example.aistudyassistantsystem.features.document.dto.DocumentUploadResponseDto;
import com.example.aistudyassistantsystem.features.document.entity.Document;
import com.example.aistudyassistantsystem.features.document.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.boot.logging.logback.RollingPolicySystemProperty.MAX_FILE_SIZE;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private static final String UPLOAD_DIR = "uploads/";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB Max limit example
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain", "text/markdown", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "image/png", "image/jpg", "image/jpeg");

    private static final List<String> ALLOWED_EXTENSIONS = List.of("pdf", "docx", "txt", "md", "pptx", "png", "jpg", "jpeg");


    // --- CUSTOM VALIDATION METHODS ---
    private void validateMultipartFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File cannot be empty or null.");
        }

        validateFileType(file);
        validateFileExtension(file);
        validateFileSize(file);
    }

    private void validateFileType(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {

            throw new FileUploadException("Only PDF, DOCX, TXT, MD, PPTX, PNG, JPG, JPEG files are allowed");
        }
    }

    private void validateFileExtension(MultipartFile file) {

        String fileName = file.getOriginalFilename();

        if (fileName == null || !fileName.contains(".")) {
            throw new FileUploadException("Invalid file name configuration.");
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {

            throw new FileUploadException("Unsupported file type: " + extension);
        }
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException("File size exceeds maximum permitted limit of 10MB.");
        }
    }


    private void validateDuplicateFile(String originalFileName) {
        if (documentRepository.existsByFileName(originalFileName)) {
            throw new FileUploadException("File already exists with the name: " + originalFileName);
        }
    }


    @Override
    public DocumentUploadResponseDto uploadFile(MultipartFile file) {
        // Apply validations
        validateMultipartFile(file);

        String originalFileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        validateDuplicateFile(originalFileName);
        try {

            String uploadDir = System.getProperty("user.dir") + "/" + UPLOAD_DIR;
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
            String filePath = uploadDir + uniqueFileName;
            file.transferTo(new File(filePath));

            Document document = new Document();
            document.setFileName(originalFileName);
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setFilePath(filePath);
            document.setUploadedAt(LocalDateTime.now());
            Document savedFile = documentRepository.save(document);
            return mapToResponseDto(savedFile);

        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file due to server disk error: : " + e.getMessage());
        }
    }


    @Override
    public List<DocumentUploadResponseDto> uploadMultipleFiles(List<MultipartFile> files) {


        if (files == null || files.isEmpty()) {
            throw new FileUploadException("Batch files collection cannot be empty.");
        }

        List<DocumentUploadResponseDto> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {

            // Skips processing intentionally if array slot is blank but processes the rest
            if (file.isEmpty()) {
                continue;
            }


            // Validates constraints for every individual loop item
            validateMultipartFile(file);
            String originalFileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
            validateDuplicateFile(originalFileName);

            try {

                String uploadDir = System.getProperty("user.dir") + "/" + UPLOAD_DIR;
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
                String filePath = uploadDir + uniqueFileName;
                file.transferTo(new File(filePath));

                Document document = new Document();
                document.setFileName(originalFileName);
                document.setFileType(file.getContentType());
                document.setFileSize(file.getSize());
                document.setFilePath(filePath);
                document.setUploadedAt(LocalDateTime.now());

                Document savedFile = documentRepository.save(document);
                uploadedFiles.add(mapToResponseDto(savedFile));


            } catch (IOException e) {
                throw new FileUploadException("Failed to upload file : " + file.getOriginalFilename());
            }
        }

        return uploadedFiles;
    }


    @Override
    public List<DocumentUploadResponseDto> getAllFiles() {
        return documentRepository.findAll().stream().map(this::mapToResponseDto).toList();
    }

    // Helper method to keep mapping DRY (Don't Repeat Yourself)
    private DocumentUploadResponseDto mapToResponseDto(Document document) {
        return DocumentUploadResponseDto.builder().id(document.getId()).fileName(document.getFileName()).fileType(document.getFileType()).fileSize(document.getFileSize()).filePath(document.getFilePath()).build();
    }
}


