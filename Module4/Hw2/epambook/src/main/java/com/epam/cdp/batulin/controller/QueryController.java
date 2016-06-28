package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.cqrs.handler.QueryHandler;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryController {
    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    private QueryHandler queryHandler;

    @RequestMapping(value = "/user/{user}/friend/{friend}/timeline", method = RequestMethod.GET)
    public List<Note> getUsersFriendNotes(@PathVariable String user, @PathVariable String friend) {
        List<Note> notes = queryHandler.getUsersFriendNotes(user, friend);
        logger.debug("Returning user's (" + user + ") friend with username " + friend + " notes: " + notes);
        return notes;
    }

    @RequestMapping(value = "/user/{username}/timeline", method = RequestMethod.GET)
    public List<Note> getUsersNotes(@PathVariable String username) {
        List<Note> notes = queryHandler.getUsersNotes(username);
        logger.debug("Returning users notes by username = " + username + ", " + notes);
        return notes;
    }

    @RequestMapping(value = "/user/{username}/friend", method = RequestMethod.GET)
    public List<User> getFriends(@PathVariable String username) {
        User user = queryHandler.getUserByUsername(username);
        List<User> friends = user.getFriends();
        logger.debug("Returning list of friends for username = " + username + ", " + friends);
        return friends;
    }

    public void setQueryHandler(QueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }
}