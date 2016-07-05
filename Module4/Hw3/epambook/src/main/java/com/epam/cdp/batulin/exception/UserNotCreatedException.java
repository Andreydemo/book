package com.epam.cdp.batulin.exception;

public class UserNotCreatedException extends RuntimeException {
    public UserNotCreatedException() {
    }

    public UserNotCreatedException(String message) {
        super(message);
    }

    public UserNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotCreatedException(Throwable cause) {
        super(cause);
    }
}
