package com.epam.cdp.batulin.service;

import com.epam.cdp.batulin.entity.Note;

import java.util.List;

public interface NoteService {
    Note addNote(Note note, String username);

    List<Note> getUsersFriendNotes(String user, String friend);

    Note addNoteToFriendsTimeline(String username, String friendUsername, Note note);

    List<Note> getUsersNotes(String username);
}
