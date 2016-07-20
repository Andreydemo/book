package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.dto.LoginDto;
import com.epam.cdp.batulin.dto.UserDto;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.Role;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.entity.validator.CreateUser;
import com.epam.cdp.batulin.entity.validator.UpdateUser;
import com.epam.cdp.batulin.exception.UserNotCreatedException;
import com.epam.cdp.batulin.exception.UserNotUpdatedException;
import com.epam.cdp.batulin.facade.UserFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private UserFacade userFacade;

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Validated(CreateUser.class) User user, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            logger.debug("User cannot be created, reason:" + message);
            throw new UserNotCreatedException(message);
        }
        return userFacade.createUser(user);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("authentication.name == #username AND hasRole('ROLE_USER')")
    public UserDto updateUser(@PathVariable String username, @Validated(UpdateUser.class) @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            logger.debug("User cannot be updated, reason:" + message);
            throw new UserNotUpdatedException(message);
        }
        user.setUsername(username);
        return userFacade.updateUser(user);
    }

    @RequestMapping(value = "/user/{name}/friend", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authentication.name == #name AND hasRole('ROLE_USER')")
    public List<UserDto> addToFriendList(@PathVariable String name, @RequestBody User userToAdd) {
        return userFacade.addToFriendList(name, userToAdd);
    }

    @RequestMapping(value = "/user/{username}/friend", method = RequestMethod.GET)
    @PreAuthorize("authentication.name == #username OR hasRole('ROLE_ADMIN')")
    public List<UserDto> getFriends(@PathVariable String username) {
        return userFacade.getFriends(username);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable String username) {
        return userFacade.getUser(username);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        try {
            request.login(loginDto.getUsername(), loginDto.getPassword());
        } catch (ServletException e) {
            logger.warn("Unable to login.", e);
        }
    }

    @RequestMapping(value = "/user/{username}/timeline", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authentication.name == #username AND hasRole('ROLE_USER')")
    public Note addNoteToUsersTimeline(@PathVariable String username, @RequestBody Note note) {
        note.setUsername(username);
        return userFacade.addNoteToUsersTimeline(note);
    }

    @RequestMapping(value = "/user/{user}/friend/{friend}/timeline", method = RequestMethod.GET)
    @PreAuthorize("authentication.name == #user OR hasRole('ROLE_ADMIN')")
    public List<Note> getUsersFriendNotes(@PathVariable String user, @PathVariable String friend) {
        return userFacade.getUsersFriendNotes(user, friend);
    }

    @RequestMapping(value = "/user/{user}/friend/{friend}/timeline", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authentication.name == #user AND hasRole('ROLE_USER')")
    public Note addNoteToFriendsTimeline(@PathVariable String user, @PathVariable String friend, @RequestBody Note note) {
        note.setUsername(user);
        return userFacade.addNoteToFriendsTimeline(friend, note);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{username}/timeline")
    @PreAuthorize("authentication.name == #username OR hasRole('ROLE_ADMIN')")
    public List<Note> getUsersNotes(@PathVariable String username) {
        return userFacade.getUsersNotes(username);
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}