package service.impl;

import dao.TicketDao;
import model.Event;
import model.Ticket;
import model.User;
import org.apache.log4j.Logger;
import service.TicketService;

import java.util.List;

public class TicketServiceImpl implements TicketService {
    private static final Logger logger = Logger.getLogger(TicketServiceImpl.class);
    private TicketDao ticketDao;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.debug("Booking ticket");
        Ticket ticket = ticketDao.bookTicket(userId, eventId, place, category);
        logger.debug("Booked ticket: " + ticket);
        return ticket;
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
    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }
}
