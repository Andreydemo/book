package service.impl;

import dao.EventDao;
import model.Event;
import org.apache.log4j.Logger;
import service.EventService;

import java.util.Date;
import java.util.List;

public class EventServiceImpl implements EventService {
    private static final Logger logger = Logger.getLogger(EventServiceImpl.class);
    private EventDao eventDao;

    @Override
    public Event getEventById(long id) {
        Event event = eventDao.getEventById(id);
        logger.debug("Returning event by id: " + id + ", " + event);
        return event;
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        List<Event> events = eventDao.getEventsByTitle(title, pageSize, pageNum);
        logger.debug("Returning events by title: " + title + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        List<Event> events = eventDao.getEventsForDay(day, pageSize, pageNum);
        logger.debug("Returning events for day: " + day + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public Event createEvent(Event event) {
        logger.debug("Creating event: " + event);
        return eventDao.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        logger.debug("Updating event: " + event);
        return eventDao.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        logger.debug("Deleting event with id: " + eventId);
        return eventDao.deleteEvent(eventId);
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
