package com.example.aistudyassistantsystem.features.documentChunk.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentChunkResponse {

    private Long id;

    private Integer chunkIndex;

    private String chunkText;
}
