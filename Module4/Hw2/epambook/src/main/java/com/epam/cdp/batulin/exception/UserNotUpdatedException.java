package com.epam.cdp.batulin.exception;

public class UserNotUpdatedException extends RuntimeException {
    public UserNotUpdatedException() {
    }

    public UserNotUpdatedException(String message) {
        super(message);
    }

    public UserNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotUpdatedException(Throwable cause) {
        super(cause);
    }
}
