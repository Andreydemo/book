package com.epam.cdp.batulin.service;

import com.epam.cdp.batulin.entity.Note;

import java.util.List;

public interface NoteService {
    Note addNote(Note note);

    List<Note> getUsersNotes(String username);
}
