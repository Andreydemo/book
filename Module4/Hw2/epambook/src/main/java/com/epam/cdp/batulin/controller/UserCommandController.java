package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.cqrs.command.AddFriendCommand;
import com.epam.cdp.batulin.cqrs.command.UserUpdateCommand;
import com.epam.cdp.batulin.cqrs.handler.CommandHandler;
import com.epam.cdp.batulin.entity.validator.CreateUser;
import com.epam.cdp.batulin.entity.validator.UpdateUser;
import com.epam.cdp.batulin.exception.UserNotCreatedException;
import com.epam.cdp.batulin.exception.UserNotUpdatedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCommandController {
    private static final Logger logger = LoggerFactory.getLogger(UserCommandController.class);

    @Autowired
    private CommandHandler commandHandler;

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Validated(CreateUser.class) UserUpdateCommand command, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            logger.debug("User cannot be created, reason:" + message);
            throw new UserNotCreatedException(message);
        }
        commandHandler.createUser(command);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable String username, @Validated(UpdateUser.class) @RequestBody UserUpdateCommand command, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            logger.debug("User cannot be updated, reason:" + message);
            throw new UserNotUpdatedException(message);
        }

        command.setUsername(username);
        commandHandler.updateUser(command);
    }

    @RequestMapping(value = "/user/{name}/friend", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addToFriendList(@PathVariable String name, @RequestBody AddFriendCommand command, BindingResult result) {
        command.setAdderUsername(name);
        commandHandler.addFriend(command);
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
}
