package com.epam.cdp.dao;

import com.epam.cdp.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public interface EventDao {
    Event getEventById(long id);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(Date day, int pageSize, int pageNum);

    Event createEvent(Event event);

    Event updateEvent(Event event);

    boolean deleteEvent(long eventId);

    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

//    void setStorage(Storage storage);
}
