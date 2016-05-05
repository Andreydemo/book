package dao.impl;

import dao.EventDao;
import model.Event;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import storage.Storage;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class EventDaoImpl implements EventDao {
    public static final String EVENT_NAMESPACE = "event:";
    private static final Logger logger = Logger.getLogger(EventDaoImpl.class);
    private Storage storage;

    @Override
    public Event getEventById(long id) {
        Event event = storage.getEntityById(EVENT_NAMESPACE + id);
        logger.debug("Returning event by id: " + event);
        return event;
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        Predicate<Event> predicate = e -> e.getTitle().contains(title);
        List<Event> events = storage.getElementsByPredicate(EVENT_NAMESPACE, predicate, (a, b) -> a.getTitle().compareTo(b.getTitle()), pageSize, pageNum);
        logger.debug("Returning events by title: " + title + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        Predicate<Event> predicate = e -> DateUtils.isSameDay(e.getDate(), day);
        List<Event> events = storage.getElementsByPredicate(EVENT_NAMESPACE, predicate, (a, b) -> a.getTitle().compareTo(b.getTitle()), pageSize, pageNum);
        logger.debug("Returning events for day: " + day + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public Event createEvent(Event event) {
        logger.debug("Creating event: " + event);
        return storage.put(EVENT_NAMESPACE + event.getId(), event);
    }

    @Override
    public Event updateEvent(Event event) {
        logger.debug("Updating event: " + event);
        return createEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        logger.debug("Deleting event with id: " + eventId);
        return storage.delete(EVENT_NAMESPACE + eventId);
    }

    @Override
    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}