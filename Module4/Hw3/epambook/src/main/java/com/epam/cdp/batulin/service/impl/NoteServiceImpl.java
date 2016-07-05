package com.epam.cdp.batulin.service.impl;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.repository.NoteRepository;
import com.epam.cdp.batulin.service.NoteService;
import com.epam.cdp.batulin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Note addNote(Note note, String username) {
        User user = userService.getUserByUsername(username);
        note.setUser(user);
        Note createdNote = noteRepository.save(note);
        userService.addNoteToUsersTimeline(user, note);
        logger.debug("Created note: " + createdNote + " by user: " + user);
        return createdNote;
    }

    @Override
    public List<Note> getUsersFriendNotes(String username, String friendUsername) {
        User user = userService.getUserByUsername(username);
        User friend = userService.getUserByUsername(friendUsername);
        userService.checkIfUsersAreFriends(user, friend);
        logger.debug("Returning timeline of user: " + username);
        return friend.getTimeline();
    }

    @Transactional
    @Override
    public Note addNoteToFriendsTimeline(String username, String friendUsername, Note note) {
        User user = userService.getUserByUsername(username);
        User friend = userService.getUserByUsername(friendUsername);
        userService.checkIfUsersAreFriends(user, friend);

        note.setUser(user);
        Note createdNote = noteRepository.save(note);
        userService.addNoteToUsersTimeline(friend, note);
        logger.debug("Added note to friend (" + friendUsername + ") timeline: " + note + " by user: " + username);
        return createdNote;
    }

    @Override
    public List<Note> getUsersNotes(String username) {
        User user = userService.getUserByUsername(username);
        logger.debug("Returning timeline of user: " + username);
        return user.getTimeline();
    }

    public void setNoteRepository(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}