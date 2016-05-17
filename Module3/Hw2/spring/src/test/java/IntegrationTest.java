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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/config/context.xml")
@Transactional
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

        Event event = new EventImpl(10, "testEvent", new Date(), new BigDecimal("1.00"));
        Event registeredEvent = bookingFacade.createEvent(event);
        bookingFacade.bookTicket(registeredUser.getId(), registeredEvent.getId(), 1, Ticket.Category.PREMIUM);
        UserAccount userAccountAfterBooking = bookingFacade.getUserAccountByUserId(registeredUser.getId());
        assertEquals(userAccount.getBalance().subtract(event.getTicketPrice()), userAccountAfterBooking.getBalance());
    }
}