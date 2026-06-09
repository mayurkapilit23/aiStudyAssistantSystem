package com.example.aistudyassistantsystem.features.document.repository;

import com.example.aistudyassistantsystem.features.document.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByFileHash(String fileHash);

}


