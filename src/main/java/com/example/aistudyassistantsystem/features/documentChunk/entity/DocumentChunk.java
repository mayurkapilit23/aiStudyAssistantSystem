package com.example.aistudyassistantsystem.features.documentChunk.entity;

import com.example.aistudyassistantsystem.features.document.entity.Document;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_chunks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer chunkIndex;

    @Lob
    @Column(
            columnDefinition = "TEXT",
            nullable = false
    )
    private String chunkText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "document_id",
            nullable = false
    )
    private Document document;

    @Column(
            columnDefinition = "vector(384)"
    )
    private float[] embedding;
}



