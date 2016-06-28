package com.epam.cdp.batulin.cqrs.handler.impl;

import com.epam.cdp.batulin.cqrs.aggregateRoot.UserAggregateRoot;
import com.epam.cdp.batulin.cqrs.command.AddFriendCommand;
import com.epam.cdp.batulin.cqrs.command.AddNoteCommand;
import com.epam.cdp.batulin.cqrs.command.UserUpdateCommand;
import com.epam.cdp.batulin.cqrs.event.UserEvent;
import com.epam.cdp.batulin.cqrs.event.store.EventStream;
import com.epam.cdp.batulin.cqrs.event.store.UserEventStore;
import com.epam.cdp.batulin.cqrs.handler.CommandHandler;
import com.epam.cdp.batulin.cqrs.handler.EventHandler;
import com.epam.cdp.batulin.exception.UserNotCreatedException;
import com.epam.cdp.batulin.exception.UserNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommandHandlerImpl implements CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandlerImpl.class);

    @Autowired
    private UserEventStore userEventStore;

    @Autowired
    private EventHandler eventHandler;

    @Override
    public void addNote(AddNoteCommand command) {
        EventStream eventStream = loadEventStream(command.getAssigneeUsername());
        long oldVersion = eventStream.getVersion();

        UserAggregateRoot userAggregateRoot = new UserAggregateRoot(eventStream.getEvents());
        UserEvent event = userAggregateRoot.addNote(command);

        long newVersion = eventStream.getVersion();
        save(command.getUsername(), oldVersion, newVersion, event);
    }

    @Override
    public void createUser(UserUpdateCommand command) {
        if (userEventStore.loadEventStream(command.getUsername()) != null)
            throw new UserNotCreatedException("Username is already taken");

        UserAggregateRoot userAggregateRoot = new UserAggregateRoot();
        UserEvent event = userAggregateRoot.updateUser(command);
        save(command.getUsername(), 1, 1,  event);
    }

    @Override
    public void updateUser(UserUpdateCommand command) {
        EventStream eventStream = loadEventStream(command.getUsername());

        long oldVersion = eventStream.getVersion();
        UserAggregateRoot userAggregateRoot = new UserAggregateRoot(eventStream.getEvents());
        UserEvent event = userAggregateRoot.updateUser(command);
        long newVersion = eventStream.getVersion();
        save(command.getUsername(), oldVersion, newVersion,  event);
    }

    @Override
    @Transactional
    public void addFriend(AddFriendCommand command) {
        addFriendInternal(command);
        String friendUsername= command.getFriendUsername();
        command.setFriendUsername(command.getUsername());
        command.setAdderUsername(friendUsername);
        addFriendInternal(command);
    }

    private void addFriendInternal(AddFriendCommand command) {
        String username = command.getUsername();
        EventStream userEventStream = loadEventStream(username);
        loadEventStream(command.getFriendUsername());
        long oldVersion = userEventStream.getVersion();
        UserAggregateRoot userAggregateRoot = new UserAggregateRoot(userEventStream.getEvents());
        UserEvent event = userAggregateRoot.addFriend(command);
        long newVersion = userEventStream.getVersion();

        save(username, oldVersion, newVersion, event);
    }

    @Transactional
    private void save(String username, long oldVersion, long newVersion, UserEvent event) {
        userEventStore.appendToStream(username, oldVersion, newVersion, event);
        eventHandler.handle(event);
    }

    private EventStream loadEventStream(String username) {
        EventStream userEventStream = userEventStore.loadEventStream(username);

        if (userEventStream == null) {
            logger.warn("Cannot find user event stream by username = " + username);
            throw new UserNotFoundException("user with username = " + username + " cannot be found");
        }
        return userEventStream;
    }

    public void setUserEventStore(UserEventStore userEventStore) {
        this.userEventStore = userEventStore;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
}