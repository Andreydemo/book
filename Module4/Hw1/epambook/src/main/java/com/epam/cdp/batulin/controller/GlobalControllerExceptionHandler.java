package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.exception.NoteNotAddedException;
import com.epam.cdp.batulin.exception.UserNotCreatedException;
import com.epam.cdp.batulin.exception.UserNotFoundException;
import com.epam.cdp.batulin.exception.UsersAreNotFriendsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(NoteNotAddedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNoteNotAddedException(NoteNotAddedException e) {
        logger.warn("Note cannot be added, sending error with status bad request");
        return new Error(HttpStatus.BAD_REQUEST.value(), "Note cannot be added: " + e.getMessage());
    }

    @ExceptionHandler(UserNotCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleUserNotCreatedException(UserNotCreatedException e) {
        logger.warn("User cannot be created, sending error with status bad request");
        return new Error(HttpStatus.BAD_REQUEST.value(), "User cannot be created: " + e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleUserNotFoundException(UserNotFoundException e) {
        logger.warn("User cannot be found, sending error with status bad request");
        return new Error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(UsersAreNotFriendsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleUsersAreNotFriendsException(UsersAreNotFriendsException e) {
        return new Error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
