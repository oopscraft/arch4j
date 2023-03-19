package org.oopscraft.arch4j.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DataNotFoundException
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * constructor
     * @param message
     */
    public NotFoundException(String message){
        super(message);
    }

    /**
     * constructor
     * @param message
     * @param t
     */
    public NotFoundException(String message, Throwable t){
        super(message, t);
    }

}
