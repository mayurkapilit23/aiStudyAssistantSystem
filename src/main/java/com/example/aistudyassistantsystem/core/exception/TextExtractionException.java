package com.example.aistudyassistantsystem.core.exception;

// 2. For failures during parsing/processing files
public class TextExtractionException extends RuntimeException {
    public TextExtractionException(String message) {
        super(message);
    }
}
