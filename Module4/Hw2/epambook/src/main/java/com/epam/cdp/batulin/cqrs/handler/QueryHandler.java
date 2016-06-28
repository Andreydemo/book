package com.epam.cdp.batulin.cqrs.handler;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;

import java.util.List;

public interface QueryHandler {
    List<Note> getUsersFriendNotes(String user, String friend);

    List<Note> getUsersNotes(String username);

    User getUserByUsername(String username);
}
