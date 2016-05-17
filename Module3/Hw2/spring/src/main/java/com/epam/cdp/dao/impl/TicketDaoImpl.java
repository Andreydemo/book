package com.epam.cdp.dao.impl;

import com.epam.cdp.dao.TicketDao;
import com.epam.cdp.dao.mapper.TicketRowMapper;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TicketDaoImpl implements TicketDao {
    private static final Logger logger = Logger.getLogger(TicketDaoImpl.class);
    private JdbcTemplate jdbcTemplate;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.debug("Booking ticket");
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("ticket").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("eventId", eventId);
        parameters.put("userId", userId);
        parameters.put("category", category);
        parameters.put("place", place);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return getTicketById(key.longValue());
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        String sql = "Select * from ticket where userId = ? limit ?, ?";
        List<Ticket> tickets = jdbcTemplate.query(sql, new Object[]{user.getId(), pageSize * pageNum - pageSize, pageSize}, new TicketRowMapper());
        logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        String sql = "Select * from ticket where eventId = ? limit ?, ?";
        List<Ticket> tickets = jdbcTemplate.query(sql, new Object[]{event.getId(), pageSize * pageNum - pageSize, pageSize}, new TicketRowMapper());
        logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + ", " + tickets);
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        String sql = "Delete from ticket where id = ?";
        logger.debug("Canceling ticket with id: " + ticketId);
        return jdbcTemplate.update(sql, ticketId) != 0;
    }

    public Ticket getTicketById(long id) {
        String sql = "Select id, eventId, userId, category, place from ticket where id = ?";
        List<Ticket> tickets = jdbcTemplate.query(sql, new Object[]{id}, new TicketRowMapper());
        logger.debug("Returning ticket by id: " + id + ", = " + tickets);
        return tickets.isEmpty() ? null : tickets.get(0);
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
