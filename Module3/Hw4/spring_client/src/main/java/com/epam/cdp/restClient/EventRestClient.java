package com.epam.cdp.restClient;

import com.epam.cdp.model.Event;

import java.util.Date;
import java.util.List;

public interface EventRestClient {
    Event getEventById(long eventId);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(Date day, int pageSize, int pageNum);

    Event createEvent(Event event);

    void updateEvent(Event event);

    void deleteEvent(long eventId);

    void call();
}
