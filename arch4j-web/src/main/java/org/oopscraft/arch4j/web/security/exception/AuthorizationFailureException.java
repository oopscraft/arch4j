package org.oopscraft.arch4j.web.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthorizationFailureException extends RuntimeException {

    public AuthorizationFailureException(String message){
        super(message);
    }
}
