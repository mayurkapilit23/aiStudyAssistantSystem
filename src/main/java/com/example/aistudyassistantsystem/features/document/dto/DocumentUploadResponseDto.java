package com.example.aistudyassistantsystem.features.document.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentUploadResponseDto {

    private Long id;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String filePath;
}
