package com.epam.cdp.batulin.cqrs.event.store;

import com.epam.cdp.batulin.cqrs.event.UserEvent;
import com.epam.cdp.batulin.cqrs.repository.UserEventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ConcurrentModificationException;

@Component
public class UserEventStoreImpl implements UserEventStore {
    @Autowired
    UserEventRepository repository;

    @Override
    public EventStream loadEventStream(String username) {
        History history = repository.findOne(username);

        if (history == null)
            return null;
        return history.getEventStream();
    }

    @Override
    public void appendToStream(String username, long oldVersion, long newVersion, UserEvent... changes) {
        if (oldVersion != newVersion)
            throw new ConcurrentModificationException("Aggregate is being updated at the same time");

        History history = repository.findOne(username);
        if (history == null) {
            history = new History();
            history.setUsername(username);
        }

        if (changes != null && changes.length > 0) {
            if (history.getEventStream() == null) {
                history.setEventStream(new EventStream());
            }

            for (UserEvent event : changes) {
                history.getEventStream().getEvents().add(event);
            }
            history.getEventStream().incrementAndGetVersion();
            repository.save(history);
        }
    }

    public void setRepository(UserEventRepository repository) {
        this.repository = repository;
    }
}