package com.epam.cdp.service.impl;

import com.epam.cdp.dao.EventDao;
import com.epam.cdp.exception.ApplicationException;
import com.epam.cdp.model.Event;
import com.epam.cdp.service.EventService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private static final Logger logger = Logger.getLogger(EventServiceImpl.class);
    EventDao eventDao;

    @Override
    public Event getEventById(long id) {
        Event event = eventDao.getEventById(id);
        logger.debug("Returning event by id: " + id + ", " + event);
        return event;
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        if (title == null) {
            logger.debug("Title is null, returning empty collection");
            return Collections.emptyList();
        }
        List<Event> events = eventDao.getEventsByTitle(title, pageSize, pageNum);
        logger.debug("Returning events by title: " + title + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        if (day == null) {
            logger.debug("Day is null, returning empty collection");
            return Collections.emptyList();
        }
        List<Event> events = eventDao.getEventsForDay(day, pageSize, pageNum);
        logger.debug("Returning events for day: " + day + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public Event createEvent(Event event) {
        if (event == null) {
            logger.debug("Event is null, returning null");
            return null;
        }
        logger.debug("Creating event: " + event);
        try {
            return eventDao.createEvent(event);
        } catch (Exception e) {
            logger.error("Event cannot be created, message: " + e.getMessage());
            throw new ApplicationException("Event cannot be created", e);
        }
    }

    @Override
    public Event updateEvent(Event event) {
        if (event == null) {
            logger.debug("Event is null, returning null");
            return null;
        }
        logger.debug("Updating event: " + event);
        try {
            return eventDao.updateEvent(event);
        } catch (Exception e) {
            logger.error("Event cannot be updated, message: " + e.getMessage());
            throw new ApplicationException("Event cannot be updated", e);
        }
    }

    @Override
    public boolean deleteEvent(long eventId) {
        logger.debug("Deleting event with id: " + eventId);
        return eventDao.deleteEvent(eventId);
    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
