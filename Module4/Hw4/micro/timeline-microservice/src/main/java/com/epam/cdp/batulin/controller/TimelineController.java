package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.exception.NoteNotAddedException;
import com.epam.cdp.batulin.service.NoteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

@RestController
public class TimelineController {
    private static final Logger logger = LoggerFactory.getLogger(TimelineController.class);

    @Autowired
    private NoteService noteService;

    @RequestMapping(value = "/timeline", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Note addNote(@RequestBody @Valid Note note, BindingResult result) {
        checkIfHasErrors(result);
        Note addedNote = noteService.addNote(note);
        logger.debug("Note that was added: " + addedNote);
        return addedNote;
    }

    @RequestMapping(value = "/timeline/{username}", method = RequestMethod.GET)
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

    @ExceptionHandler(NoteNotAddedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNoteNotAddedException(NoteNotAddedException e) {
        logger.warn("Note cannot be added, sending error with status bad request");
        return new Error(HttpStatus.BAD_REQUEST.value(), "Note cannot be added: " + e.getMessage());
    }

    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }
}