package com.superbank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        final Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors()
            .forEach((error) -> {

                if (error instanceof FieldError fieldError) {
                    String fieldName = fieldError.getField(); // Nom du champ
                    String errorMessage = error.getDefaultMessage();     // Message d'erreur
                    errors.put(fieldName, errorMessage);
                }
            });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
