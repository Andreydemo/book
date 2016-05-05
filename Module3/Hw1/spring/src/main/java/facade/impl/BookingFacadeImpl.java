package facade.impl;

import facade.BookingFacade;
import model.Event;
import model.Ticket;
import model.User;
import org.apache.log4j.Logger;
import service.EventService;
import service.TicketService;
import service.UserService;

import java.util.Date;
import java.util.List;

public class BookingFacadeImpl implements BookingFacade {
    private static final Logger logger = Logger.getLogger(BookingFacadeImpl.class);
    private EventService eventService;
    private UserService userService;
    private TicketService ticketService;

    public BookingFacadeImpl(EventService eventService, UserService userService, TicketService ticketService) {
        this.eventService = eventService;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @Override
    public Event getEventById(long eventId) {
        Event event = eventService.getEventById(eventId);
        logger.debug("Returning event by id: " + eventId + ", " + event);
        return event;
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        List<Event> events = eventService.getEventsByTitle(title, pageSize, pageNum);
        logger.debug("Returning events by title: " + title + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        List<Event> events = eventService.getEventsForDay(day, pageSize, pageNum);
        logger.debug("Returning events for day: " + day + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public Event createEvent(Event event) {
        logger.debug("Creating event: " + event);
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        logger.debug("Updating event: " + event);
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        logger.debug("Deleting event with id: " + eventId);
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        User user = userService.getUserById(userId);
        logger.debug("Returning user by id: " + userId + ", " + user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userService.getUserByEmail(email);
        logger.debug("Returning user by email: " + email + ", " + user);
        return user;
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        List<User> users = userService.getUsersByName(name, pageSize, pageNum);
        logger.debug("Returning users by name: " + name + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + users);
        return users;
    }

    @Override
    public User createUser(User user) {
        logger.debug("Creating user: " + user);
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        logger.debug("Updating user: " + user);
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        logger.debug("Deleting user by id " + userId);
        return userService.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.debug("Booking ticket");
        Ticket ticket = ticketService.bookTicket(userId, eventId, place, category);
        logger.debug("Booked ticket: " + ticket);
        return ticket;
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketService.getBookedTickets(user, pageSize, pageNum);
        logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketService.getBookedTickets(event, pageSize, pageNum);
        logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        logger.debug("Canceling ticket with id: " + ticketId);
        return ticketService.cancelTicket(ticketId);
    }
}
