package com.superbank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Exception ex) {
        LOGGER.error("Une exception inattendue est survenue : {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: the topic is on track." );
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
