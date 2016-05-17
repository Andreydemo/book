package com.epam.cdp.service;

import com.epam.cdp.dao.EventDao;
import com.epam.cdp.dao.TicketDao;
import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.EventImpl;
import com.epam.cdp.model.impl.TicketImpl;
import com.epam.cdp.model.impl.UserAccountImpl;
import com.epam.cdp.model.impl.UserImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import com.epam.cdp.service.impl.TicketServiceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    @Mock
    EventDao eventDao;

    @Mock
    UserAccountDao userAccountDao;

    @Before
    public void setUp() {
        ticketDao = mock(TicketDao.class);
        eventDao = mock(EventDao.class);
        userAccountDao = mock(UserAccountDao.class);
        ticketService = new TicketServiceImpl();
        ticketService.setTicketDao(ticketDao);
        ticketService.setUserAccountDao(userAccountDao);
        ticketService.setEventDao(eventDao);
    }

    @Test
    public void whenBookTicketThenOneIsBooked() {
        Ticket ticket = new TicketImpl(Long.MIN_VALUE, 1, 1, Ticket.Category.STANDART, 1);
        when(ticketDao.bookTicket(anyLong(), anyLong(), anyInt(), any(Ticket.Category.class))).thenReturn(ticket);
        when(userAccountDao.getUserAccountByUserId(anyLong())).thenReturn(new UserAccountImpl(1, 1, new BigDecimal("20.00")));
        when(eventDao.getEventById(anyLong())).thenReturn(new EventImpl(1, "title", new Date(), new BigDecimal("10.00")));
        Ticket result = ticketService.bookTicket(1, 1, 1, Ticket.Category.STANDART);
        assertEquals(ticket, result);
    }

    @Test
    public void whenGetBookedTicketsByUserThenListWithTicketsIsReturned() {
        Ticket ticket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDART);
        Ticket ticket2 = ticketDao.bookTicket(1, 2, 1, Ticket.Category.PREMIUM);
        User user = new UserImpl(1, "user", "user@user.com");
        List<Ticket> expected = Arrays.asList(ticket, ticket2);
        when(ticketDao.getBookedTickets(any(User.class), anyInt(), anyInt())).thenReturn(expected);
        List<Ticket> result = ticketService.getBookedTickets(user, 4, 1);
        assertEquals(expected, result);
    }

    @Test
    public void whenGetBookedTicketsByEventThenListWithTicketsIsReturned() {
        Ticket ticket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDART);
        Ticket ticket2 = ticketDao.bookTicket(2, 1, 1, Ticket.Category.PREMIUM);
        List<Ticket> expected = Arrays.asList(ticket, ticket2);
        Event event = new EventImpl(1, "event", new Date(9999), new BigDecimal(BigInteger.ONE));
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

