package com.example.aistudyassistantsystem.features.documentContent.entity;

import com.example.aistudyassistantsystem.features.document.entity.Document;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "document_id",
            nullable = false,
            unique = true
    )
    private Document document;

    @Lob
    @Column(name = "extracted_text", columnDefinition = "TEXT", nullable = false)
    private String extractedText;
}
