package service;

import dao.TicketDao;
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
    private TicketService ticketService;

    @Mock
    TicketDao ticketDao;

    @Before
    public void setUp() {
        ticketDao = mock(TicketDao.class);
        ticketService = new TicketServiceImpl();
        ticketService.setTicketDao(ticketDao);
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
        Ticket ticket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDARD);
        Ticket ticket2 = ticketDao.bookTicket(1, 2, 1, Ticket.Category.PREMIUM);
        User user = new UserImpl(1, "user", "user@user.com");
        List<Ticket> expected = Arrays.asList(ticket, ticket2);
        when(ticketDao.getBookedTickets(any(User.class), anyInt(), anyInt())).thenReturn(expected);
        List<Ticket> result = ticketService.getBookedTickets(user, 4, 1);
        assertEquals(expected, result);
    }

    @Test
    public void whenGetBookedTicketsByEventThenListWithTicketsIsReturned() {
        Ticket ticket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDARD);
        Ticket ticket2 = ticketDao.bookTicket(2, 1, 1, Ticket.Category.PREMIUM);
        List<Ticket> expected = Arrays.asList(ticket, ticket2);
        Event event = new EventImpl(1, "event", new Date(9999));
        when(ticketDao.getBookedTickets(any(Event.class), anyInt(), anyInt())).thenReturn(expected);
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

