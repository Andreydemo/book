package com.epam.cdp.batulin.cqrs.handler.impl;

import com.epam.cdp.batulin.cqrs.handler.QueryHandler;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.exception.UserNotFoundException;
import com.epam.cdp.batulin.exception.UsersAreNotFriendsException;
import com.epam.cdp.batulin.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryHandlerImpl implements QueryHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueryHandlerImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Note> getUsersFriendNotes(String username, String friendUsername) {
        User user = getUserByUsername(username);
        User friend = getUserByUsername(friendUsername);
        checkIfUsersAreFriends(user, friend);
        logger.debug("Returning timeline of user: " + username);
        return friend.getTimeline();
    }

    @Override
    public List<Note> getUsersNotes(String username) {
        User user = getUserByUsername(username);
        logger.debug("Returning timeline of user: " + username);
        return user.getTimeline();
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("Cannot find user by username = " + username);
            throw new UserNotFoundException("user with username = " + username + " cannot be found");
        }

        logger.debug("Returning user by username = " + username + ", " + user);
        return user;
    }

    private void checkIfUsersAreFriends(User user, User friend) {
        if (!user.getFriends().contains(friend) && !friend.getFriends().contains(user)) {
            String message = "Users " + user.getUsername() + " and " + friend.getUsername() + " are not friends, throwing UsersAreNotFriendsException";
            logger.warn(message);
            throw new UsersAreNotFriendsException(message);
        }
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
