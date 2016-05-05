package service;

import model.Event;
import dao.EventDao;

import java.util.Date;
import java.util.List;

public interface EventService {
    void setEventDao(EventDao eventDao);

    Event getEventById(long id);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(Date day, int pageSize, int pageNum);

    Event createEvent(Event event);

    Event updateEvent(Event event);

    boolean deleteEvent(long eventId);
}
