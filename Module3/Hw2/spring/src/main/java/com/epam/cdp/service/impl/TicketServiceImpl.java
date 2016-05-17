package com.epam.cdp.service.impl;

import com.epam.cdp.dao.EventDao;
import com.epam.cdp.dao.TicketDao;
import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.exception.ApplicationException;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.UserAccount;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.epam.cdp.service.TicketService;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Logger logger = Logger.getLogger(TicketServiceImpl.class);
    private TicketDao ticketDao;
    private UserAccountDao userAccountDao;
    private EventDao eventDao;

    @Transactional
    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.debug("Booking ticket");
        Event event = eventDao.getEventById(eventId);
        UserAccount userAccount = userAccountDao.getUserAccountByUserId(userId);
        if (userAccount.getBalance().compareTo(event.getTicketPrice()) >= 0) {
            logger.debug("Enough amount to buy a ticket, withdrawing " + event.getTicketPrice() + " from users account");
            userAccountDao.withdraw(userId, event.getTicketPrice());
            Ticket ticket = ticketDao.bookTicket(userId, eventId, place, category);
            logger.debug("Booked ticket: " + ticket);
            return ticket;
        }
        logger.warn("Unable to book a ticket, ticket price is " + event.getTicketPrice() + ", but account has " + userAccount.getBalance());
        throw new ApplicationException("Ticket cannot be booked because user does not have enough money on his account");
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketDao.getBookedTickets(user, pageSize, pageNum);
        logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketDao.getBookedTickets(event, pageSize, pageNum);
        logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        logger.debug("Canceling ticket with id: " + ticketId);
        return ticketDao.cancelTicket(ticketId);
    }

    @Override
    @Autowired
    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    @Autowired
    public void setUserAccountDao(UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }

    @Override
    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
