package org.oopscraft.arch4j.web.login.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthenticationFailureException extends RuntimeException {

    public AuthenticationFailureException(String message){
        super(message);
    }
}
