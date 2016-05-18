package dao.impl;

import dao.TicketDao;
import model.Event;
import model.Ticket;
import model.User;
import model.impl.TicketImpl;
import org.apache.log4j.Logger;
import storage.Storage;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static storage.NamespaceConstants.EVENT_NAMESPACE;
import static storage.NamespaceConstants.TICKET_NAMESPACE;
import static storage.NamespaceConstants.USER_NAMESPACE;

public class TicketDaoImpl implements TicketDao {
    private static final Logger logger = Logger.getLogger(TicketDaoImpl.class);
    private Storage storage;
    private AtomicLong id = new AtomicLong(Long.MIN_VALUE);

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.debug("Booking ticket");
        Ticket ticket = new TicketImpl(id.getAndIncrement(), eventId, userId, category, place);
        logger.debug("Booked ticket: " + ticket);
        return storage.put(TICKET_NAMESPACE + ticket.getId(), ticket);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        Predicate<Ticket> predicate = e -> e.getUserId() == user.getId();
        Comparator<Ticket> comparator = (o1, o2) -> {
            Event event = storage.getEntityById(EVENT_NAMESPACE + o1.getEventId());
            Event anotherEvent = storage.getEntityById(EVENT_NAMESPACE + o1.getEventId());
            return anotherEvent.getDate().compareTo(event.getDate());
        };
        List<Ticket> tickets = storage.getElementsByPredicate(TICKET_NAMESPACE, predicate, comparator, pageSize, pageNum);
        logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        Predicate<Ticket> predicate = e -> e.getEventId() == event.getId();
        Comparator<Ticket> comparator = (o1, o2) -> {
            User user = storage.getEntityById(USER_NAMESPACE + o1.getUserId());
            User anotherUser = storage.getEntityById(USER_NAMESPACE + o2.getUserId());
            return user.getEmail().compareTo(anotherUser.getEmail());
        };
        List<Ticket> tickets = storage.getElementsByPredicate(TICKET_NAMESPACE, predicate, comparator, pageSize, pageNum);
        logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        logger.debug("Canceling ticket with id: " + ticketId);
        return storage.delete(TICKET_NAMESPACE + ticketId);
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
