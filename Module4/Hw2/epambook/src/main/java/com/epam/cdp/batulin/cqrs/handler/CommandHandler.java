package com.epam.cdp.batulin.cqrs.handler;

import com.epam.cdp.batulin.cqrs.command.AddFriendCommand;
import com.epam.cdp.batulin.cqrs.command.AddNoteCommand;
import com.epam.cdp.batulin.cqrs.command.UserUpdateCommand;

public interface CommandHandler {

    void addNote(AddNoteCommand command);

    void createUser(UserUpdateCommand command);

    void updateUser(UserUpdateCommand command);

    void addFriend(AddFriendCommand command);
}