package com.epam.cdp.batulin.facade;

import com.epam.cdp.batulin.dto.UserDto;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.service.NoteService;
import com.epam.cdp.batulin.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultUserFacade implements UserFacade {
    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @Override
    public UserDto createUser(User user) {
        User createdUser = userService.createUser(user);
        return convertUserDtoFromUserEntity(createdUser);
    }

    @Override
    public UserDto updateUser(User user) {
        User updatedUser = userService.updateUser(user);
        return convertUserDtoFromUserEntity(updatedUser);
    }

    @Override
    public List<UserDto> addToFriendList(String name, User userToAdd) {
        User user = userService.addUserToFriendList(name, userToAdd.getUsername());
        return user.getFriends().stream().map(this::convertUserDtoFromUserEntity).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getFriends(String username) {
        User user = userService.getUserByUsername(username);
        List<User> friends = user.getFriends();
        return friends.stream().map(this::convertUserDtoFromUserEntity).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(String username) {
        User user = userService.getUserByUsername(username);
        return convertUserDtoFromUserEntity(user);
    }

    @Override
    public List<Note> getUsersFriendNotes(String user, String friend) {
        return userService.getUsersFriendNotes(user, friend);
    }

    @Override
    public Note addNoteToFriendsTimeline(String friend, Note note) {
        return userService.addNoteToFriendsTimeline(friend, note);
    }

    @Override
    public Note addNoteToUsersTimeline(Note note) {
        return noteService.addNote(note);
    }

    @Override
    public List<Note> getUsersNotes(String username) {
        return noteService.getUsersNotes(username);
    }

    private UserDto convertUserDtoFromUserEntity(User user) {
        return new UserDto(user.getName(), user.getUsername(), user.getDateOfBirth());
    }

    @Override
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }
}
