package com.epam.cdp.batulin.cqrs.aggregateRoot;

import com.epam.cdp.batulin.cqrs.event.UserEvent;
import com.epam.cdp.batulin.cqrs.event.FriendAddedEvent;
import com.epam.cdp.batulin.cqrs.event.NoteAddedEvent;
import com.epam.cdp.batulin.cqrs.event.UserConcerningEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserState {
    private static final Logger logger = LoggerFactory.getLogger(UserState.class);

    private String name;
    private String username;
    private Date dateOfBirth;
    private List<String> friends;
    private List<Note> timeline;

    public UserState(List<UserEvent> events) {
        friends = new ArrayList<>();
        timeline = new ArrayList<>();
        events.forEach(this::mutate);
    }

    public UserState() {
    }

    public void mutate(UserEvent event) {
        try {
            Method method = this.getClass().getDeclaredMethod("handle", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error("Cannot find handle method", e);
        }
    }

    private void handle(NoteAddedEvent event) {
        timeline.add(new Note(event.getUsername(), event.getNoteText()));
    }

    private void handle(UserConcerningEvent event) {
        name = event.getName();
        username = event.getUsername();
        dateOfBirth = event.getDateOfBirth();
    }

    private void handle(FriendAddedEvent event) {
        friends.add(event.getFriendUsername());
    }

    @Override
    public String toString() {
        return "UserState{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", friends=" + friends +
                ", timeline=" + timeline +
                '}';
    }
}