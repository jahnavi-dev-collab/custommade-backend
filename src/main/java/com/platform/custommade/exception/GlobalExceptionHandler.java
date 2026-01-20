package com.platform.custommade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

     //❌ Invalid credentials / generic errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // ❌ Validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return new ResponseEntity<>(
                new ApiError(HttpStatus.BAD_REQUEST.value(), errorMsg),
                HttpStatus.BAD_REQUEST
        );
    }
//
//    // ❌ Fallback (unexpected errors)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleAll(Exception ex) {
//        return new ResponseEntity<>(
//                new ApiError(
//                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                        "Something went wrong"
//                ),
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleConflict(ConflictException ex) {
        return Map.of(
                "status", 409,
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", 500);
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now());

        ex.printStackTrace(); // IMPORTANT: prints actual error

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
