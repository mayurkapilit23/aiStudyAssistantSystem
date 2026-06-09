package com.example.aistudyassistantsystem.core.exception;

import com.example.aistudyassistantsystem.core.utils.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileUploadException(FileUploadException ex) {


        // 1. Check if the incoming exception is the specific duplicate flag
        if ("File already exists".equalsIgnoreCase(ex.getMessage())) {
            ApiResponse<Object> response = ApiResponse.error("File already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict
        }

        // 2. Fallback for all other general file upload validation errors
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {

//        String errorMessage = ex.getBindingResult()
//                .getFieldError()
//                .getDefaultMessage();

        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DocumentContentNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDocumentContentNotFoundException(DocumentContentNotFoundException ex) {

        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404 Not Found
    }


    // Document not found maps to 404 Not Found
    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Extraction failure maps to 422 Unprocessable Entity (or 500 depending on preference)
    @ExceptionHandler(TextExtractionException.class)
    public ResponseEntity<ApiResponse<Object>> handleTextExtractionException(TextExtractionException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
