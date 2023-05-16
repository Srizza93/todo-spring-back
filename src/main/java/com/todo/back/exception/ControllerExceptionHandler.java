package com.todo.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerExceptionHandler {

    public static ResponseEntity<Error> handleInternalServerException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
    }
}
