package com.epam.cdp.dao.impl;

import com.epam.cdp.dao.EventDao;
import com.epam.cdp.dao.mapper.EventRowMapper;
import com.epam.cdp.model.Event;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventDaoImpl implements EventDao {
    private static final Logger logger = Logger.getLogger(EventDaoImpl.class);
    private JdbcTemplate jdbcTemplate;

    @Override
    public Event getEventById(long id) {
        String sql = "Select id, title, date, ticketPrice from event where id =?";
        List<Event> events = jdbcTemplate.query(sql, new Object[]{id}, new EventRowMapper());
        logger.debug("Returning event by id: " + id + ", = " + events);
        return events.isEmpty() ? null : events.get(0);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        String sql = "Select id, title, date, ticketPrice from event where title like ? limit ?,?";
        List<Event> events = jdbcTemplate.query(sql, new Object[]{"%" + title + "%", pageSize * pageNum - pageSize, pageSize}, new EventRowMapper());
        logger.debug("Returning events by title: " + title + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        String sql = "Select id, title, date, ticketPrice from event where cast(date AS DATE) = cast(? AS DATE) limit ?,?";
        return jdbcTemplate.query(sql, new Object[]{new java.sql.Date(day.getTime()), pageSize * pageNum - pageSize, pageSize}, new EventRowMapper());
    }

    @Override
    public Event createEvent(Event event) {
        logger.debug("Creating event: " + event);
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("event").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", event.getTitle());
        parameters.put("date", new Timestamp(event.getDate().getTime()));
        parameters.put("ticketPrice", event.getTicketPrice());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return getEventById(key.longValue());
    }

    @Override
    public Event updateEvent(Event event) {
        String sql = "Update event set title=?, date=?, ticketPrice=? where id=?";
        logger.debug("Updating event: " + event);
        jdbcTemplate.update(sql, event.getTitle(), new Timestamp(event.getDate().getTime()), event.getTicketPrice().doubleValue(), event.getId());
        return getEventById(event.getId());
    }

    @Override
    public boolean deleteEvent(long eventId) {
        String sql = "Delete from event where id = ?";
        logger.debug("Deleting event with id: " + eventId);
        return jdbcTemplate.update(sql, eventId) != 0;
    }

    @Autowired
    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}