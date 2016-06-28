package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.cqrs.command.AddNoteCommand;
import com.epam.cdp.batulin.cqrs.handler.CommandHandler;
import com.epam.cdp.batulin.exception.NoteNotAddedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class NoteCommandController {
    @Autowired
    private CommandHandler commandHandler;

    private static final Logger logger = LoggerFactory.getLogger(NoteCommandController.class);

    @RequestMapping(value = "/user/{username}/timeline", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addNoteToUsersTimeline(@PathVariable String username, @RequestBody @Valid AddNoteCommand command, BindingResult result) {
        checkIfHasErrors(result);
        command.setAssigneeUsername(username);
        command.setAuthorUsername(username);
        commandHandler.addNote(command);
    }

    @RequestMapping(value = "/user/{user}/friend/{friend}/timeline", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addNoteToFriendsTimeline(@PathVariable String user, @PathVariable String friend, @RequestBody @Valid AddNoteCommand command, BindingResult result) {
        checkIfHasErrors(result);
        command.setAssigneeUsername(friend);
        command.setAuthorUsername(user);
        commandHandler.addNote(command);
    }

    private void checkIfHasErrors(BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            logger.debug("Note cannot be added, reason:" + message);
            throw new NoteNotAddedException(message);
        }
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
}
