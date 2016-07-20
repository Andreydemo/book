package com.epam.cdp.batulin.service;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getUserByUsername(String username);

    User addUserToFriendList(String username, String friendUserName);

    void checkIfUsersAreFriends(User user, User friend);

    User updateUser(User user);

    Note addNoteToFriendsTimeline(String friend, Note note);

    List<Note> getUsersFriendNotes(String user, String friend);
}
