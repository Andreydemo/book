package service;

import dao.EventDao;
import dao.TicketDao;
import dao.UserDao;
import model.Event;
import model.Ticket;
import model.User;
import model.impl.EventImpl;
import model.impl.TicketImpl;
import model.impl.UserImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import service.impl.TicketServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketServiceTest {
    private TicketServiceImpl ticketService;

    @Mock
    TicketDao ticketDao;

    @Mock
    EventDao eventDao;

    @Mock
    UserDao userDao;

    @Before
    public void setUp() {
        ticketDao = mock(TicketDao.class);
        eventDao = mock(EventDao.class);
        userDao = mock(UserDao.class);
        ticketService = new TicketServiceImpl();
        ticketService.setTicketDao(ticketDao);
        ticketService.setEventDao(eventDao);
        ticketService.setUserDao(userDao);
    }

    @Test
    public void whenBookTicketThenOneIsBooked() {
        Ticket ticket = new TicketImpl(Long.MIN_VALUE, 1, 1, Ticket.Category.STANDARD, 1);
        when(ticketDao.bookTicket(anyLong(), anyLong(), anyInt(), any(Ticket.Category.class))).thenReturn(ticket);
        Ticket result = ticketService.bookTicket(1, 1, 1, Ticket.Category.STANDARD);
        assertEquals(ticket, result);
    }

    @Test
    public void whenGetBookedTicketsByUserThenListWithTicketsIsReturned() {
        Ticket ticket = new TicketImpl(1, 1, 1, Ticket.Category.STANDARD, 1);
        Ticket ticket2 = new TicketImpl(2, 2, 1, Ticket.Category.PREMIUM, 1);
        User user = new UserImpl(1, "user", "user@user.com");
        List<Ticket> expected = Arrays.asList(ticket, ticket2);
        when(ticketDao.getBookedTickets(any(User.class), anyInt(), anyInt())).thenReturn(expected);
        when(eventDao.getEventById(1)).thenReturn(new EventImpl(1, "title", new Date()));
        when(eventDao.getEventById(2)).thenReturn(new EventImpl(2, "anotherTitle", new Date(22222)));
        List<Ticket> result = ticketService.getBookedTickets(user, 4, 1);
        assertEquals(expected, result);
    }

    @Test
    public void whenGetBookedTicketsByEventThenListWithTicketsIsReturned() {
        Ticket ticket = new TicketImpl(1, 1, 1, Ticket.Category.STANDARD, 1);
        Ticket ticket2 = new TicketImpl(2, 2, 2, Ticket.Category.PREMIUM, 1);
        List<Ticket> expected = Arrays.asList(ticket, ticket2);
        Event event = new EventImpl(1, "event", new Date(9999));
        when(ticketDao.getBookedTickets(any(Event.class), anyInt(), anyInt())).thenReturn(expected);
        when(userDao.getUserById(1)).thenReturn(new UserImpl(1, "name", "a@a.com"));
        when(userDao.getUserById(2)).thenReturn(new UserImpl(2, "anotherName", "z@z.com"));
        List<Ticket> result = ticketService.getBookedTickets(event, 4, 1);
        assertEquals(expected, result);
    }

    @Test
    public void whenCancelTicketThenOneIsCanceled() {
        when(ticketDao.cancelTicket(anyLong())).thenReturn(true);
        Ticket ticket = new TicketImpl(1, 1, 1, Ticket.Category.PREMIUM, 1);
        boolean result = ticketService.cancelTicket(ticket.getId());
        assertTrue(result);
    }
}

