package com.project.TaskManger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> responseEntity(NotFoundException e){
        ApiException apiException=new ApiException(
                e.getMessage(),
                e,
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now());
        return new ResponseEntity<>(apiException,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> responseEntity(DuplicateResourceException e){
        ApiException apiException=new ApiException(
                e.getMessage(),
                e,
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> responseEntity(ApiRequestException e){
        ApiException apiException=new ApiException(
                e.getMessage(),
                e,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }

}
