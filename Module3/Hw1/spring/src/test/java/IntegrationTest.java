import facade.BookingFacade;
import model.Event;
import model.Ticket;
import model.User;
import model.impl.EventImpl;
import model.impl.UserImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import storage.Storage;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/config/context.xml")
public class IntegrationTest {
    private BookingFacade bookingFacade;
    Storage storage;

    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("/config/context.xml");
        bookingFacade = context.getBean(BookingFacade.class);
        storage = context.getBean(Storage.class);
    }

    @Test
    public void testRegisterUserThenBuyTicketAndThenCancelIt() {
        User user = new UserImpl(20, "John", "john@john.com");
        User registeredUser = bookingFacade.createUser(user);
        assertEquals(user, registeredUser);

        Event event = new EventImpl(10, "testEvent", new Date());
        Event registeredEvent = bookingFacade.createEvent(event);
        assertEquals(event, registeredEvent);

        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 1, Ticket.Category.PREMIUM);
        assertEquals(registeredUser, storage.getEntityById("user:" + registeredUser.getId()));
        assertEquals(ticket, storage.getEntityById("ticket:" + ticket.getId()));

        boolean result = bookingFacade.cancelTicket(ticket.getId());
        assertTrue(result);
        assertNull(storage.getEntityById("ticket:" + ticket.getId()));
    }

    @Test
    public void testRegisterUserChangeItsInfoAndDeleteIt() {
        User user = new UserImpl(20, "John", "john@john.com");
        User registeredUser = bookingFacade.createUser(user);
        assertEquals(user, registeredUser);

        User userToUpdate = new UserImpl(20, "John", "newEmail@john.com");
        User updatedUser = bookingFacade.updateUser(userToUpdate);
        assertEquals(userToUpdate, updatedUser);
        assertEquals("newEmail@john.com", updatedUser.getEmail());

        boolean result = bookingFacade.deleteUser(user.getId());
        assertTrue(result);
        assertNull(storage.getEntityById("user:" + user.getId()));
    }

    @Test
    public void testCreateEventUpdateItAndDeleteIt() {
        Event event = new EventImpl(10, "testEvent", new Date());
        Event registeredEvent = bookingFacade.createEvent(event);
        assertEquals(event, registeredEvent);

        Event eventToUpdate = new EventImpl(10, "anotherEvent", new Date(232));
        Event updatedEvent = bookingFacade.updateEvent(eventToUpdate);
        assertEquals(eventToUpdate, updatedEvent);
        assertEquals("anotherEvent", updatedEvent.getTitle());

        boolean result = bookingFacade.deleteEvent(updatedEvent.getId());
        assertTrue(result);
        assertNull(storage.getEntityById("event:" + updatedEvent.getId()));
    }
}
