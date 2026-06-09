package com.example.aistudyassistantsystem.features.documentContent.repository;

import com.example.aistudyassistantsystem.features.documentContent.entity.DocumentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentContentRepository
        extends JpaRepository<DocumentContent, Long> {

    Optional<DocumentContent> findByDocument_Id(
            Long documentId
    );
}
