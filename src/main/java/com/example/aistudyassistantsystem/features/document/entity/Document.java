package com.example.aistudyassistantsystem.features.document.entity;


import com.example.aistudyassistantsystem.features.documentContent.entity.DocumentContent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String filePath;

    private LocalDateTime uploadedAt;

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DocumentContent documentContent;
}












