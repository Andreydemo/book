package service.impl;

import dao.EventDao;
import dao.TicketDao;
import dao.UserDao;
import model.Event;
import model.Ticket;
import model.User;
import org.apache.log4j.Logger;
import service.TicketService;

import java.util.List;

public class TicketServiceImpl implements TicketService {
    private static final Logger logger = Logger.getLogger(TicketServiceImpl.class);
    private TicketDao ticketDao;
    private EventDao eventDao;
    private UserDao userDao;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.debug("Booking ticket");
        if (isTicketCanBeBooked(userId, eventId)) {
            Ticket ticket = ticketDao.bookTicket(userId, eventId, place, category);
            logger.debug("Booked ticket: " + ticket);
            return ticket;
        }
        return null;
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketDao.getBookedTickets(user, pageSize, pageNum);
        tickets.sort((o1, o2) -> {
            Event event = eventDao.getEventById(o1.getId());
            Event anotherEvent = eventDao.getEventById(o2.getId());
            return anotherEvent.getDate().compareTo(event.getDate());
        });
        logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketDao.getBookedTickets(event, pageSize, pageNum);
        tickets.sort((o1, o2) -> {
            User user = userDao.getUserById(o1.getId());
            User anotherUser = userDao.getUserById(o2.getId());
            return user.getEmail().compareTo(anotherUser.getEmail());
        });
        logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        logger.debug("Canceling ticket with id: " + ticketId);
        return ticketDao.cancelTicket(ticketId);
    }

    private boolean isTicketCanBeBooked(long userId, long eventId) {
        if (eventDao.getEventById(eventId) == null) {
            logger.debug("Such event does not exist, ticket cannot be booked, returning null");
            return false;
        }
        if (userDao.getUserById(userId) == null) {
            logger.debug("Such user does not exist, ticket cannot be booked, returning null");
            return false;
        }
        return true;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
