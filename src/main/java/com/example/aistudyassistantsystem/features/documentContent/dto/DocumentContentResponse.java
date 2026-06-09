package com.example.aistudyassistantsystem.features.documentContent.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentContentResponse {

    private Long id;

    private Long documentId;

    private String extractedText;
}

