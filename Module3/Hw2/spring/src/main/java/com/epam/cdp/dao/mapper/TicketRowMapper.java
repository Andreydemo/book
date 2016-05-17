package com.epam.cdp.dao.mapper;

import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.impl.TicketImpl;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketRowMapper implements RowMapper<Ticket> {
    @Override
    public Ticket mapRow(ResultSet resultSet, int i) throws SQLException {
        Ticket ticket = new TicketImpl();
        ticket.setId(resultSet.getLong("id"));
        ticket.setEventId(resultSet.getLong("eventId"));
        ticket.setUserId(resultSet.getLong("userId"));
        ticket.setCategory(Ticket.Category.valueOf(resultSet.getString("category")));
        ticket.setPlace(resultSet.getInt("place"));
        return ticket;
    }
}
