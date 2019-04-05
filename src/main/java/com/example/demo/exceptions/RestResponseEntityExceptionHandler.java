package com.example.demo.exceptions;

import com.example.demo.model.ResponceModel;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        ResponceModel<String> responceModel = new ResponceModel<>();
        responceModel.setCode(HttpStatus.BAD_REQUEST.value());
        responceModel.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, responceModel, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
