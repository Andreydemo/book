package com.epam.cdp;

import com.epam.cdp.facade.BookingFacade;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.UserAccount;
import com.epam.cdp.model.impl.EventImpl;
import com.epam.cdp.model.impl.UserImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = "/config/root-context.xml")
@ActiveProfiles(profiles = "dev")
public class IntegrationTest {
    @Autowired
    private BookingFacade bookingFacade;

    @Test
    public void testRegisterUserThenBuyTicketAndThenCancelIt() {
        User user = new UserImpl("John", "john@john.com");
        User registeredUser = bookingFacade.createUser(user);
        assertEquals(user.getName(), registeredUser.getName());
        assertEquals(user.getEmail(), registeredUser.getEmail());

        Event event = new EventImpl("testEvent", new Date(), new BigDecimal("1.00"));
        Event registeredEvent = bookingFacade.createEvent(event);
        assertEquals(event.getTitle(), registeredEvent.getTitle());
        assertEquals(event.getTicketPrice(), registeredEvent.getTicketPrice());

        bookingFacade.refillAccount(registeredUser.getId(), new BigDecimal("200.00"));

        Ticket ticket = bookingFacade.bookTicket(registeredUser.getId(), registeredEvent.getId(), 1, Ticket.Category.PREMIUM);
        assertEquals(registeredUser.getId(), ticket.getUserId());
        assertEquals(registeredEvent.getId(), ticket.getEventId());
        assertEquals(Ticket.Category.PREMIUM, ticket.getCategory());
        assertEquals(1, ticket.getPlace());

        boolean result = bookingFacade.cancelTicket(ticket.getId());
        assertTrue(result);
    }

    @Test
    public void testRegisterUserChangeItsInfoAndDeleteIt() {
        User user = new UserImpl("John", "john@john.com");
        User registeredUser = bookingFacade.createUser(user);
        assertEquals(user.getName(), registeredUser.getName());
        assertEquals(user.getEmail(), registeredUser.getEmail());

        User userToUpdate = bookingFacade.getUserById(registeredUser.getId());
        userToUpdate.setName("newName");
        userToUpdate.setEmail("newEmail@newEmail.com");
        User updatedUser = bookingFacade.updateUser(userToUpdate);
        assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
        assertEquals(userToUpdate.getName(), updatedUser.getName());

        boolean result = bookingFacade.deleteUser(updatedUser.getId());
        assertTrue(result);
    }

    @Test
    public void testCreateEventUpdateItAndDeleteIt() {
        Event event = new EventImpl(10, "testEvent", new Date(), new BigDecimal("1.00"));
        Event registeredEvent = bookingFacade.createEvent(event);
        assertEquals(event.getTitle(), registeredEvent.getTitle());
        assertEquals(event.getTicketPrice(), registeredEvent.getTicketPrice());

        Event eventToUpdate = bookingFacade.getEventById(registeredEvent.getId());
        eventToUpdate.setTitle("newTitle");
        eventToUpdate.setTicketPrice(new BigDecimal("3.99"));
        Event updatedEvent = bookingFacade.updateEvent(eventToUpdate);
        assertEquals(eventToUpdate.getTitle(), updatedEvent.getTitle());
        assertEquals(eventToUpdate.getTicketPrice(), updatedEvent.getTicketPrice());

        boolean result = bookingFacade.deleteEvent(updatedEvent.getId());
        assertTrue(result);
    }

    @Test
    public void testRegisterUserAndRefillItsBalanceAndBookTicket() {
        User user = new UserImpl("John", "john@john.com");
        User registeredUser = bookingFacade.createUser(user);
        assertEquals(user.getName(), registeredUser.getName());
        assertEquals(user.getEmail(), registeredUser.getEmail());

        bookingFacade.refillAccount(registeredUser.getId(), new BigDecimal("400.00"));
        UserAccount userAccount = bookingFacade.getUserAccountByUserId(registeredUser.getId());
        assertEquals(registeredUser.getId(), userAccount.getUserId());
        assertEquals(new BigDecimal("400.00"), userAccount.getBalance());

        Event event = new EventImpl(10, "testEvent", new Date(), new BigDecimal("100.00"));
        Event registeredEvent = bookingFacade.createEvent(event);
        bookingFacade.bookTicket(registeredUser.getId(), registeredEvent.getId(), 1, Ticket.Category.PREMIUM);
        UserAccount userAccountAfterBooking = bookingFacade.getUserAccountByUserId(registeredUser.getId());
        assertEquals(userAccount.getBalance().subtract(event.getTicketPrice()), userAccountAfterBooking.getBalance());
    }

    @Test
    public void testGetBookedTicketsWithDefaultUser() {
        User user = new UserImpl("John", "john@john.com");
        User defaultUser = bookingFacade.createUser(user);
        bookingFacade.refillAccount(defaultUser.getId(), new BigDecimal("200.00"));
        bookingFacade.setDefaultUser(defaultUser);

        user = new UserImpl("dsadsa", "dsada@dsad.dasd");
        User registeredUser = bookingFacade.createUser(user);

        Event event = new EventImpl(10, "testEvent", new Date(), new BigDecimal("100.00"));
        Event registeredEvent = bookingFacade.createEvent(event);
        bookingFacade.bookTicket(defaultUser.getId(), registeredEvent.getId(), 1, Ticket.Category.PREMIUM);

        List<Ticket> tickets = bookingFacade.getBookedTickets(registeredUser, 10, 1);
        assertEquals(defaultUser.getId(), tickets.get(0).getUserId());
    }

    @Test
    public void testGetBookedTicketsWithDefaultEvent() {
        Event event = new EventImpl(10, "testEvent", new Date(), new BigDecimal("100.00"));
        Event defaultEvent = bookingFacade.createEvent(event);
        bookingFacade.setDefaultEvent(defaultEvent);

        User user = new UserImpl("John", "john@john.com");
        User registeredUser = bookingFacade.createUser(user);
        bookingFacade.refillAccount(registeredUser.getId(), new BigDecimal("200.00"));

        event = new EventImpl(102, "dsadsad", new Date(21321), new BigDecimal("30.10"));
        Event registeredEvent = bookingFacade.createEvent(event);
        bookingFacade.bookTicket(registeredUser.getId(), defaultEvent.getId(), 1, Ticket.Category.PREMIUM);

        List<Ticket> tickets = bookingFacade.getBookedTickets(registeredEvent, 10, 1);
        assertEquals(defaultEvent.getId(), tickets.get(0).getEventId());
    }
}