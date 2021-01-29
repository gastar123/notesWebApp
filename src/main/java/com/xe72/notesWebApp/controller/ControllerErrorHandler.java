package com.xe72.notesWebApp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

    // Нужно, т.к. если аутентификация была через rememberMe, AccessDeniedException будет отдавать не 403, а редирект
    // на страницу логина. Это зашито в ExceptionTranslationFilter
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(ex, null, headers, status, request);
    }
}
