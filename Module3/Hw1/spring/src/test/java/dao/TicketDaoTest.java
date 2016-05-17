package dao;

import dao.impl.TicketDaoImpl;
import model.Event;
import model.Ticket;
import model.User;
import model.impl.EventImpl;
import model.impl.TicketImpl;
import model.impl.UserImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import storage.Storage;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketDaoTest {
    private TicketDaoImpl ticketDao;

    @Mock
    Storage storage;

    @Before
    public void setUp() {
        storage = mock(Storage.class);
        ticketDao = new TicketDaoImpl();
        ticketDao.setStorage(storage);
    }

    @Test
    public void whenBookTicketThenOneIsBooked() {
        Ticket ticket = new TicketImpl(Long.MIN_VALUE, 1, 1, Ticket.Category.STANDARD, 1);
        when(storage.put(anyString(), anyObject())).thenReturn(ticket);
        Ticket result = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDARD);
        assertEquals(ticket, result);
    }

    @Test
    public void whenGetBookedTicketsByUserThenListWithTicketsIsReturned() {
        Ticket ticket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDARD);
        Ticket ticket2 = ticketDao.bookTicket(1, 2, 1, Ticket.Category.PREMIUM);
        User user = new UserImpl(1, "user", "user@user.com");
        List<Object> expected = Arrays.asList(ticket, ticket2);
        when(storage.getElementsByPredicate(anyString(), anyObject(), anyObject(), anyInt(), anyInt())).thenReturn(expected);
        List<Ticket> result = ticketDao.getBookedTickets(user, 4, 1);
        assertEquals(expected, result);
    }

    @Test
    public void whenGetBookedTicketsByEventThenListWithTicketsIsReturned() {
        Ticket ticket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDARD);
        Ticket ticket2 = ticketDao.bookTicket(2, 1, 1, Ticket.Category.PREMIUM);
        List<Object> expected = Arrays.asList(ticket, ticket2);
        Event event = new EventImpl(1, "event", new Date(9999));
        when(storage.getElementsByPredicate(anyString(), anyObject(), anyObject(), anyInt(), anyInt())).thenReturn(expected);
        List<Ticket> result = ticketDao.getBookedTickets(event, 4, 1);
        assertEquals(expected, result);
    }

    @Test
    public void whenCancelTicketThenOneIsCanceled() {
        when(storage.delete(anyString())).thenReturn(true);
        Ticket ticket = new TicketImpl(1, 1, 1, Ticket.Category.PREMIUM, 1);
        boolean result = ticketDao.cancelTicket(ticket.getId());
        assertTrue(result);
    }
}
