package com.epam.cdp.dao;

import com.epam.cdp.RootConfig;
import com.epam.cdp.entityHolder.EntityHolderImpl;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.EventImpl;
import com.epam.cdp.model.impl.TicketImpl;
import com.epam.cdp.model.impl.UserImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {RootConfig.class})
@ActiveProfiles(profiles = "dev")
public class TicketDaoTest {
    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private EntityHolderImpl entityHolder;

    @Test
    public void whenBookTicketThenOneIsBooked() {
        Ticket ticket = new TicketImpl(1, 1, 1, Ticket.Category.STANDART, 1);
        Ticket result = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDART);
        assertEquals(ticket.getUserId(), result.getUserId());
        assertEquals(ticket.getEventId(), result.getEventId());
        assertEquals(ticket.getPlace(), result.getPlace());
        assertEquals(ticket.getCategory(), result.getCategory());
    }

    @Test
    public void whenGetBookedTicketsByUserThenListWithTicketsIsReturned() {
        Ticket ticket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDART);
        Ticket ticket2 = ticketDao.bookTicket(1, 2, 1, Ticket.Category.PREMIUM);
        User user = new UserImpl(1, "test", "test@test.com");
        List<Object> expected = Arrays.asList(ticket, ticket2);
        List<Ticket> result = ticketDao.getBookedTickets(user, 4, 1);
        assertEquals(expected, result);
    }

    @Test
    public void whenGetBookedTicketsByEventThenListWithTicketsIsReturned() {
        Ticket ticket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDART);
        Ticket ticket2 = ticketDao.bookTicket(2, 1, 1, Ticket.Category.PREMIUM);
        List<Object> expected = Arrays.asList(ticket, ticket2);
        Event event = new EventImpl(1, "event", new Date(9999), new BigDecimal(BigInteger.ONE));
        List<Ticket> result = ticketDao.getBookedTickets(event, 4, 1);
        assertEquals(expected, result);
    }

    @Test
    public void whenCancelTicketThenOneIsCanceled() {
        Ticket bookedTicket = ticketDao.bookTicket(1, 1, 1, Ticket.Category.STANDART);
        boolean result = ticketDao.cancelTicket(bookedTicket.getId());
        assertTrue(result);
    }
}
