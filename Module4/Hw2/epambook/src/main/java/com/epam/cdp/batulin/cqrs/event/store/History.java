package com.epam.cdp.batulin.cqrs.event.store;


import org.springframework.data.annotation.Id;

public class History {
    @Id
    private String username;
    private EventStream eventStream;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public EventStream getEventStream() {
        return eventStream;
    }

    public void setEventStream(EventStream eventStream) {
        this.eventStream = eventStream;
    }

    @Override
    public String toString() {
        return "History{" +
                "username='" + username + '\'' +
                ", eventStream=" + eventStream +
                '}';
    }
}
