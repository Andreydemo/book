package com.epam.cdp.batulin.cqrs.event.store;

import com.epam.cdp.batulin.cqrs.event.UserEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class EventStream {
    private List<UserEvent> events = new ArrayList<>();
    private AtomicLong version;

    public EventStream() {
        this.version = new AtomicLong(1);
    }

    public void setEvents(List<UserEvent> events) {
        this.events = events;
    }

    public List<UserEvent> getEvents() {
        return events;
    }

    public long getVersion() {
        return version.get();
    }

    public long incrementAndGetVersion() {
        return version.incrementAndGet();
    }

    @Override
    public String toString() {
        return "EventStream{" +
                "events=" + events +
                '}';
    }
}
