package com.globant.demo.exception;

import com.globant.demo.resource.ErrorResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MeetingRoomExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResource> handleBusinessException(BusinessException exception) {

        return ResponseEntity.badRequest().body(new ErrorResource(exception.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResource> handleGeneralException(Exception exception) {

        return ResponseEntity.internalServerError().body(new ErrorResource(exception.getMessage()));
    }
}
