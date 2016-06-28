package com.epam.cdp.batulin.cqrs.event.store;

import com.epam.cdp.batulin.cqrs.event.UserEvent;

public interface UserEventStore {
    EventStream loadEventStream(String username);

    void appendToStream(String username, long oldVersion, long newVersion, UserEvent... changes);
}
