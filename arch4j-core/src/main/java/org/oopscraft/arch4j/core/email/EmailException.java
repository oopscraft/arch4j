package org.oopscraft.arch4j.core.email;

public class EmailException extends Exception {

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

}
