package com.example.aistudyassistantsystem.core.exception;

// 1. For missing database records
public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
