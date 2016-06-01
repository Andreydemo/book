package com.epam.cdp.exception;

public class TicketNotBookedException extends RuntimeException {
    public TicketNotBookedException() {
    }

    public TicketNotBookedException(String message) {
        super(message);
    }

    public TicketNotBookedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketNotBookedException(Throwable cause) {
        super(cause);
    }
}
