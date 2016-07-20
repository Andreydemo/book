package com.epam.cdp.batulin.exception;

public class UsersAreNotFriendsException extends RuntimeException {
    public UsersAreNotFriendsException() {
    }

    public UsersAreNotFriendsException(String message) {
        super(message);
    }

    public UsersAreNotFriendsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsersAreNotFriendsException(Throwable cause) {
        super(cause);
    }
}
