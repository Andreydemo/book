package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.dto.LoginDto;
import com.epam.cdp.batulin.dto.UserDto;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.entity.validator.CreateUser;
import com.epam.cdp.batulin.entity.validator.UpdateUser;
import com.epam.cdp.batulin.exception.UserNotCreatedException;
import com.epam.cdp.batulin.exception.UserNotUpdatedException;
import com.epam.cdp.batulin.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Validated(CreateUser.class) User user, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            logger.debug("User cannot be created, reason:" + message);
            throw new UserNotCreatedException(message);
        }
        User createdUser = userService.createUser(user);
        logger.debug("Created User " + user);

        return convertUserDtoFromUserEntity(createdUser);
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
        User updatedUser = userService.updateUser(user);
        logger.debug("Updated User " + user);

        return convertUserDtoFromUserEntity(updatedUser);
    }

    @RequestMapping(value = "/user/{name}/friend", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authentication.name == #name AND hasRole('ROLE_USER')")
    public List<UserDto> addToFriendList(@PathVariable String name, @RequestBody User userToAdd) {
        User user = userService.addUserToFriendList(name, userToAdd.getUsername());
        logger.debug("Added user " + userToAdd.getUsername() + " to friend list of user " + user);
        return user.getFriends().stream().map(this::convertUserDtoFromUserEntity).collect(Collectors.toList());
    }

    @RequestMapping(value = "/user/{username}/friend", method = RequestMethod.GET)
    @PreAuthorize("authentication.name == #username OR hasRole('ROLE_ADMIN')")
    public List<UserDto> getFriends(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        List<User> friends = user.getFriends();
        logger.debug("Returning list of friends for username = " + username + ", " + friends);
        return friends.stream().map(this::convertUserDtoFromUserEntity).collect(Collectors.toList());
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return convertUserDtoFromUserEntity(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        try {
            request.login(loginDto.getUsername(), loginDto.getPassword());
        } catch (ServletException e) {
            logger.warn("Unable to login.", e);
        }
    }

    private UserDto convertUserDtoFromUserEntity(User user) {
        return new UserDto(user.getName(), user.getUsername(), user.getDateOfBirth());
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}