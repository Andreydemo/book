package com.epam.cdp.service.impl;

import com.epam.cdp.dao.EventDao;
import com.epam.cdp.dao.TicketDao;
import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.dao.UserDao;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.UserAccount;
import com.epam.cdp.service.TicketService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Logger logger = Logger.getLogger(TicketServiceImpl.class);
    private TicketDao ticketDao;
    private UserAccountDao userAccountDao;
    private EventDao eventDao;
    private UserDao userDao;

    @Transactional
    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        if (place < 1 || category == null) {
            logger.debug("Not valid arguments, returning null");
            return null;
        }
        logger.debug("Booking ticket");
        if (isUserExists(userId) && isEventExists(eventId)) {
            if (isPlaceAlreadyBooked(eventId, place)) {
                logger.debug("Place " + place + " is already booked for eventId: " + eventId);
                throw new IllegalStateException("Place " + place + " is already booked for eventId: " + eventId);
            }
            Event event = eventDao.getEventById(eventId);
            UserAccount userAccount = userAccountDao.getUserAccountByUserId(userId);
            if (isEnoughMoneyOnAccount(event, userAccount)) {
                userAccountDao.withdraw(userId, event.getTicketPrice());
                Ticket ticket = ticketDao.bookTicket(userId, eventId, place, category);
                logger.debug("Booked ticket: " + ticket);
                return ticket;
            }
            logger.warn("Unable to book a ticket, ticket price is " + event.getTicketPrice() + ", but account has " + userAccount.getBalance());
        }
        logger.debug("Ticket for userId: " + userId + " and eventId: " + eventId + " and place: " + place + " cannot be booked");
        return null;
    }

    private boolean isEnoughMoneyOnAccount(Event event, UserAccount userAccount) {
        return userAccount.getBalance().compareTo(event.getTicketPrice()) >= 0;
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        if (user == null || pageSize < 1 || pageNum < 1) {
            logger.debug("Not valid arguments, returning empty collection");
            return Collections.emptyList();
        }
        List<Ticket> tickets = ticketDao.getBookedTickets(user, pageSize, pageNum);
        logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        if (event == null || pageSize < 1 || pageNum < 1) {
            logger.debug("Not valid arguments, returning empty collection");
            return Collections.emptyList();
        }
        List<Ticket> tickets = ticketDao.getBookedTickets(event, pageSize, pageNum);
        logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        logger.debug("Canceling ticket with id: " + ticketId);
        return ticketDao.cancelTicket(ticketId);
    }

    private boolean isUserExists(long userId) {
        return userDao.getUserById(userId) != null;
    }

    private boolean isEventExists(long eventId) {
        return eventDao.getEventById(eventId) != null;
    }

    private boolean isPlaceAlreadyBooked(long eventId, int place) {
        List<Ticket> bookedTickets = getBookedTickets(eventDao.getEventById(eventId), Integer.MAX_VALUE, 1);
        for (Ticket ticket : bookedTickets) {
            if (ticket.getPlace() == place)
                return true;
        }
        return false;
    }

    @Autowired
    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Autowired
    public void setUserAccountDao(UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
