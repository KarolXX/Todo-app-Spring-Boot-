package io.github.mat3e.todoapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = IllegalExceptionProcessing.class)
class IllegalExceptionsControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> illegalArgumentHandler(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> illegalStateHandler(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
