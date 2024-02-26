package com.amirtghizde.tsvanalyzer.utils.exceptions;

import org.hibernate.QueryException;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handelIllegalArgumentException(IllegalArgumentException e) {
        ApiException apiException = new ApiException(
                "( º﹃º )",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DuplicateValueException.class)
    public ResponseEntity<Object> handelDuplicateValueException(DuplicateValueException e) {
        ApiException apiException = new ApiException(
                "(⚆ᗝ⚆) (☉_ ☉)",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }
}
