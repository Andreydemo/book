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
        if (isEventExists(eventId) && isUserExists(userId)) {
            if (isPlaceAlreadyBooked(eventId, place)) {
                logger.debug("Place " + place + " is already booked for eventId: " + eventId);
                throw new IllegalStateException("Place " + place + " is already booked for eventId: " + eventId);
            }
            Ticket ticket = ticketDao.bookTicket(userId, eventId, place, category);
            logger.debug("Booked ticket: " + ticket);
            return ticket;
        }
        logger.debug("Such user or/and event does not exist, ticket cannot be booked");
        return null;
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
