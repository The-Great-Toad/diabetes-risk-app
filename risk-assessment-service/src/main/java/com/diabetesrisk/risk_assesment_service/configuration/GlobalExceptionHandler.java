package com.diabetesrisk.risk_assesment_service.configuration;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors for @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        err -> err.getField(),
                        err -> err.getDefaultMessage(),
                        (msg1, msg2) -> msg1  // Keep the first message if duplicate
                ));
        return ResponseEntity.badRequest().body(errors);
    }

    // Handle validation errors for @RequestParam, @PathVariable, or method-level validation
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        cv -> {
                            String[] pathParts = cv.getPropertyPath().toString().split("\\.");
                            return pathParts[pathParts.length - 1];  // Get the parameter name
                        },
                        cv -> cv.getMessage(),
                        (msg1, msg2) -> msg1
                ));
        return ResponseEntity.badRequest().body(errors);
    }
}
