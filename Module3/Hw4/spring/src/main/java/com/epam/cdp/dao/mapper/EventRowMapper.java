package com.epam.cdp.dao.mapper;

import com.epam.cdp.model.Event;
import com.epam.cdp.model.impl.EventImpl;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int i) throws SQLException {
        Event event = new EventImpl();
        event.setId(resultSet.getLong("id"));
        event.setDate(new Date(resultSet.getTimestamp("date").getTime()));
        event.setTitle(resultSet.getString("title"));
        event.setTicketPrice(resultSet.getBigDecimal("ticketPrice"));
        return event;
    }
}