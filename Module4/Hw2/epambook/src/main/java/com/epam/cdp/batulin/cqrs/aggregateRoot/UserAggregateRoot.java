package com.epam.cdp.batulin.cqrs.aggregateRoot;

import com.epam.cdp.batulin.cqrs.command.AddFriendCommand;
import com.epam.cdp.batulin.cqrs.command.AddNoteCommand;
import com.epam.cdp.batulin.cqrs.command.UserUpdateCommand;
import com.epam.cdp.batulin.cqrs.event.UserEvent;
import com.epam.cdp.batulin.cqrs.event.FriendAddedEvent;
import com.epam.cdp.batulin.cqrs.event.NoteAddedEvent;
import com.epam.cdp.batulin.cqrs.event.UserConcerningEvent;

import java.util.List;

public class UserAggregateRoot {
    private UserState userState;

    public UserAggregateRoot(List<UserEvent> events) {
        userState = new UserState(events);
    }

    public UserAggregateRoot() {
        userState = new UserState();
    }

    public UserEvent addNote(AddNoteCommand command) {
        NoteAddedEvent event = new NoteAddedEvent(command.getUsername(), command.getAssigneeUsername(), command.getNoteText());
        userState.mutate(event);
        return event;
    }

    public UserEvent updateUser(UserUpdateCommand command) {
        UserConcerningEvent event = new UserConcerningEvent(command.getUsername(), command.getName(), command.getDateOfBirth());
        userState.mutate(event);
        return event;
    }

    public UserEvent addFriend(AddFriendCommand command) {
        FriendAddedEvent event = new FriendAddedEvent(command.getUsername(), command.getFriendUsername());
        userState.mutate(event);
        return event;
    }

    @Override
    public String toString() {
        return "UserAggregateRoot{" +
                "userState=" + userState +
                '}';
    }
}