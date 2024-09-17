package org.oopscraft.arch4j.web.common.error;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorControllerAdvice {

    private final ErrorResponseHandler errorResponseHandler;

    @ExceptionHandler(NoSuchElementException.class)
    public void handleNoSuchElementException(HttpServletRequest request, HttpServletResponse response, NoSuchElementException exception) {
        errorResponseHandler.sendErrorResponse(request, response, HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) {
        errorResponseHandler.sendErrorResponse(request, response, HttpStatus.BAD_REQUEST, exception);
    }

}
