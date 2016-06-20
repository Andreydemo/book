package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.exception.UserNotCreatedException;
import com.epam.cdp.batulin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user, BindingResult result) {
        System.out.println(user);
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            logger.debug("User cannot be created, reason:" + message);
            throw new UserNotCreatedException(message);
        }
        User createdUser = userService.createUser(user);
        logger.debug("Created User " + user);
        return createdUser;
    }

    @RequestMapping(value = "/user/{name}/friend", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public List<User> addToFriendList(@PathVariable String name, @RequestBody User userToAdd, BindingResult result) {
        User user = userService.addUserToFriendList(name, userToAdd.getUsername());
        logger.debug("Added user " + userToAdd.getUsername() + " to friend list of user " + user);
        return user.getFriends();
    }

    @RequestMapping(value = "/user/{username}/friend", method = RequestMethod.GET)
    public List<User> getFriends(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        List<User> friends = user.getFriends();
        logger.debug("Returning list of friends for username = " + username + ", " + friends);
        return friends;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}