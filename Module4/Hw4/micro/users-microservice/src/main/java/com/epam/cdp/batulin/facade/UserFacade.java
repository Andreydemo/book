package com.epam.cdp.batulin.facade;

import com.epam.cdp.batulin.dto.UserDto;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.service.NoteService;
import com.epam.cdp.batulin.service.UserService;

import java.util.List;

public interface UserFacade {
    UserDto createUser(User user);

    UserDto updateUser(User user);

    List<UserDto> addToFriendList(String name, User userToAdd);

    List<UserDto> getFriends(String username);

    UserDto getUser(String username);

    Note addNoteToUsersTimeline(Note note);

    List<Note> getUsersFriendNotes(String user, String friend);

    Note addNoteToFriendsTimeline(String friend, Note note);

    List<Note> getUsersNotes(String username);

    void setUserService(UserService userService);

    void setNoteService(NoteService noteService);
}
