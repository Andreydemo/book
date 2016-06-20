package com.epam.cdp.batulin.service;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.exception.UserNotFoundException;
import com.epam.cdp.batulin.repository.NoteRepository;
import com.epam.cdp.batulin.service.impl.NoteServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.sql.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoteServiceTest {
    private NoteServiceImpl noteService;

    @Mock
    NoteRepository noteRepository;

    @Mock
    UserService userService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        noteRepository = mock(NoteRepository.class);
        userService = mock(UserService.class);
        noteService = new NoteServiceImpl();
        noteService.setUserService(userService);
        noteService.setNoteRepository(noteRepository);
    }

    @Test
    public void whenAddNoteTheNoteIsAdded() {
        String username = "username";
        Note note = new Note("noteText");
        User user = new User(username, "xxxx", new Date(1));
        when(userService.getUserByUsername(username)).thenReturn(user);
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note addedNote = noteService.addNote(note, username);
        assertEquals(note.getNoteText(), addedNote.getNoteText());
        assertEquals(note.getUser(), user);
    }

    @Test
    public void whenAddNoteToNonExistingUserTheUserNotFoundExceptionIsThrown() {
        String username = "username";
        exception.expect(UserNotFoundException.class);
        Note note = new Note("noteText");

        when(userService.getUserByUsername(username)).thenThrow(UserNotFoundException.class);
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        noteService.addNote(note, username);
    }

    @Test
    public void whenGetFriendNotesTheListOfNotesIsReturned() {
        String username = "username";
        String friendUsername = "friend";
        User user = new User(username, "xxxx", new Date(1));
        User friend = new User(friendUsername, "yyy", new Date(2));
        friend.getFriends().add(user);
        user.getFriends().add(friend);
        Note note = new Note("noteText");
        friend.setTimeline(asList(note));

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(userService.getUserByUsername(friendUsername)).thenReturn(friend);

        List<Note> notes = noteService.getUsersFriendNotes(username, friendUsername);
        assertEquals(notes.get(0), note);
    }

    @Test
    public void whenAddNoteToFriendsTimelineThenNoteIsAdded() {
        String username = "username";
        String friendUsername = "friend";
        User user = new User(username, "xxxx", new Date(1));
        User friend = new User(friendUsername, "yyy", new Date(2));

        friend.getFriends().add(user);
        user.getFriends().add(friend);
        Note note = new Note("noteText");

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(userService.getUserByUsername(friendUsername)).thenReturn(friend);
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note addedNote = noteService.addNoteToFriendsTimeline(username, friendUsername, note);
        assertEquals(addedNote.getNoteText(), note.getNoteText());
        assertEquals(addedNote.getUser(), user);
    }

    @Test
    public void whenGetUserNotesThenListIsReturned() {
        String username = "username";
        User user = new User(username, "xxxx", new Date(1));
        Note note = new Note("noteText");
        user.setTimeline(asList(note));

        when(userService.getUserByUsername(username)).thenReturn(user);

        List<Note> notes = noteService.getUsersNotes(username);
        assertEquals(notes.get(0), note);
    }
}