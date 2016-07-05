package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.exception.NoteNotAddedException;
import com.epam.cdp.batulin.service.NoteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

@RestController
public class NoteController {
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteService noteService;

    @RequestMapping(value = "/user/{username}/timeline", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authentication.name == #username AND hasRole('ROLE_USER')")
    public Note addNoteToUsersTimeline(@PathVariable String username, @RequestBody @Valid Note note, BindingResult result) {
        checkIfHasErrors(result);
        Note addedNote = noteService.addNote(note, username);
        logger.debug("Note that was added: " + addedNote);
        return addedNote;
    }

    @RequestMapping(value = "/user/{user}/friend/{friend}/timeline", method = RequestMethod.GET)
    @PreAuthorize("authentication.name == #user OR hasRole('ROLE_ADMIN')")
    public List<Note> getUsersFriendNotes(@PathVariable String user, @PathVariable String friend) {
        List<Note> notes = noteService.getUsersFriendNotes(user, friend);
        logger.debug("Returning user's (" + user + ") friend with username " + friend + " notes: " + notes);
    return notes;
    }

    @RequestMapping(value = "/user/{user}/friend/{friend}/timeline", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authentication.name == #user AND hasRole('ROLE_USER')")
    public Note addNoteToFriendsTimeline(@PathVariable String user, @PathVariable String friend, @RequestBody @Valid Note note, BindingResult result) {
        checkIfHasErrors(result);
        Note addedNote = noteService.addNoteToFriendsTimeline(user, friend, note);
        logger.debug("Note was added to user's(" + user + ") friend (" + friend + ") timeline: " + addedNote);
        return addedNote;
    }

    @RequestMapping(value = "/user/{username}/timeline", method = RequestMethod.GET)
    @PreAuthorize("authentication.name == #username OR hasRole('ROLE_ADMIN')")
    public List<Note> getUsersNotes(@PathVariable String username) {
        List<Note> notes = noteService.getUsersNotes(username);
        logger.debug("Returning users notes by username = " + username + ", " + notes);
        return notes;
    }

    private void checkIfHasErrors(BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            logger.debug("Note cannot be added, reason:" + message);
            throw new NoteNotAddedException(message);
        }
    }

    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }
}