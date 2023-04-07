package org.oopscraft.arch4j.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Validation exception handler
     * @param ex exception
     * @param headers headers
     * @param status status
     * @param request request
     * @return response entity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", "Bad Request");
        body.put("message", ex.getClass().getSimpleName());
        List<FieldError> allFieldErrors = ex.getFieldErrors();
        if(!allFieldErrors.isEmpty()) {
            FieldError fieldError = allFieldErrors.get(0);
            String objectName = fieldError.getObjectName();
            String fieldName = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            body.put("message", String.format("[%s.%s] %s", objectName, fieldName, defaultMessage));
        }
        body.put("path", ((ServletWebRequest)request).getRequest().getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
