package org.oopscraft.arch4j.core.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthenticationFailureException extends RuntimeException {

    public AuthenticationFailureException() {
        super();
    }

    public AuthenticationFailureException(String message){
        super(message);
    }
}
