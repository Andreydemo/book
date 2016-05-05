package service;

import dao.TicketDao;
import model.Event;
import model.Ticket;
import model.User;

import java.util.List;

public interface TicketService {
    void setTicketDao(TicketDao ticketDao);

    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);
}
