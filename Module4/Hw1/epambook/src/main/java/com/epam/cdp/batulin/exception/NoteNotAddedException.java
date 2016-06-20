package com.epam.cdp.batulin.exception;

public class NoteNotAddedException extends RuntimeException {

    public NoteNotAddedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoteNotAddedException(Throwable cause) {
        super(cause);
    }

    public NoteNotAddedException() {
    }

    public NoteNotAddedException(String message) {
        super(message);
    }
}
