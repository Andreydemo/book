package com.epam.cdp.batulin.cqrs.handler.impl;

import com.epam.cdp.batulin.cqrs.event.UserEvent;
import com.epam.cdp.batulin.cqrs.event.FriendAddedEvent;
import com.epam.cdp.batulin.cqrs.event.NoteAddedEvent;
import com.epam.cdp.batulin.cqrs.event.UserConcerningEvent;
import com.epam.cdp.batulin.cqrs.handler.EventHandler;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.repository.NoteRepository;
import com.epam.cdp.batulin.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

@Component
public class EventHandlerImpl implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(EventHandlerImpl.class);

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void handle(UserEvent event) {
        try {
            Method method = this.getClass().getDeclaredMethod("handle", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error("Cannot find handle method", e);
        }
    }

    private void handle(NoteAddedEvent event) {
        User user = userRepository.findByUsername(event.getUsername());

        Note note = new Note(event.getNoteText());
        note.setUser(user);
        noteRepository.save(note);

        user = userRepository.findByUsername(event.getAssigneeUsername());
        if (user.getTimeline() == null)
            user.setTimeline(new ArrayList<>());

        user.getTimeline().add(note);
        userRepository.save(user);
    }

    private void handle(UserConcerningEvent event) {
        User user = userRepository.findByUsername(event.getUsername());

        if (user == null) {
            user = new User();
            user.setUsername(event.getUsername());
        }

        Date dateOfBirth = event.getDateOfBirth();
        if (dateOfBirth != null)
            user.setDateOfBirth(dateOfBirth);

        String name = event.getName();
        if (name != null)
            user.setName(name);

        userRepository.save(user);
    }

    private void handle(FriendAddedEvent event) {
        User user = userRepository.findByUsername(event.getUsername());
        User friend = userRepository.findByUsername(event.getFriendUsername());
        user.getFriends().add(friend);
        userRepository.save(user);
    }

    public void setNoteRepository(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}