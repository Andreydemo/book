package com.epam.cdp.service;

import com.epam.cdp.dao.EventDao;
import com.epam.cdp.dao.TicketDao;
import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;

import java.util.List;

public interface TicketService {
    void setTicketDao(TicketDao ticketDao);

    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);

    void setUserAccountDao(UserAccountDao userAccountDao);

    void setEventDao(EventDao eventDao);
}
