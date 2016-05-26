package com.epam.cdp.dao;

import com.epam.cdp.RootConfig;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.impl.EventImpl;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {RootConfig.class})
@ActiveProfiles(profiles = "dev")
public class EventDaoTest {
    @Autowired
    private EventDao eventDao;

    @Test
    public void whenGetEventWithIdThenEventIsReturned() {
        Event event = new EventImpl(1, "test", new Date(9999), new BigDecimal("333.33"));
        assertEquals(event, eventDao.getEventById(1));
    }

    @Test
    public void whenGetEventsByTitleThenEventsAreReturned() {
        List<Object> events = new ArrayList<>();
        events.add(new EventImpl(2, "event", new Date(9999), new BigDecimal("1.00")));
        assertEquals(((Event) events.get(0)).getTitle(), eventDao.getEventsByTitle("event", 1, 1).get(0).getTitle());
    }

    @Test
    public void whenGetEventsByTitleWithPageSizeThenNumberOfEventsAreReturned() {
        List<Object> events = new ArrayList<>();
        events.add(new EventImpl(2, "event", new Date(9999), new BigDecimal("1.00")));
        events.add(new EventImpl(3, "event", new Date(9999), new BigDecimal("1.00")));
        List<Event> result = eventDao.getEventsByTitle("event", 2, 1);
        assertEquals(((Event) events.get(0)).getTitle(), result.get(0).getTitle());
        assertEquals(((Event) events.get(1)).getTitle(), result.get(1).getTitle());
    }

    @Test
    public void whenGetEventsByTitleWithHigherPageSizeThenNumberOfActualEventsAreReturned() {
        List<Object> events = new ArrayList<>();
        events.add(new EventImpl(2, "event", new Date(9999), new BigDecimal("1.00")));
        events.add(new EventImpl(3, "event", new Date(9999), new BigDecimal("1.00")));
        events.add(new EventImpl(4, "event", new Date(9999), new BigDecimal("1.00")));
        List<Event> result = eventDao.getEventsByTitle("event", 20, 1);
        assertEquals(((Event) events.get(0)).getTitle(), result.get(0).getTitle());
        assertEquals(((Event) events.get(1)).getTitle(), result.get(1).getTitle());
        assertEquals(((Event) events.get(2)).getTitle(), result.get(2).getTitle());
        assertEquals(3, result.size());
    }

    @Test
    public void whenGetEventsByTitleSecondPageThenSecondPageIsReturned() {
        List<Object> events = new ArrayList<>();
        events.add(new EventImpl(2, "event", new Date(9999), new BigDecimal("1.00")));
        List<Event> result = eventDao.getEventsByTitle("event", 2, 2);
        assertEquals(((Event) events.get(0)).getTitle(), result.get(0).getTitle());
        assertEquals(1, result.size());
    }

    @Test
    public void whenGetEventsForDayWithOnePageSizeThenOneEventIsReturned() {
        Event expected = new EventImpl(2, "event", new Date(9999), new BigDecimal("1.00"));
        List<Event> events = eventDao.getEventsForDay(new Date(9999), 1, 1);
        assertTrue(DateUtils.isSameDay(expected.getDate(), events.get(0).getDate()));
    }

    @Test
    public void whenCreateEventThenOneIsCreated() {
        Event event = new EventImpl("testEvent", new Date(9999), new BigDecimal("1.00"));
        Event result = eventDao.createEvent(event);
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getTicketPrice(), result.getTicketPrice());
        assertEquals(event.getDate(), result.getDate());
    }

    @Test
    public void whenUpdateEventThenOneIsUpdated() {
        Event event = new EventImpl(22, "testEvent", new Date(), new BigDecimal("1.00"));
        Event createdEvent = eventDao.createEvent(event);
        event = new EventImpl(createdEvent.getId(), "REPRESENTATION", new Date(2222), new BigDecimal(BigInteger.ONE));
        Event result = eventDao.updateEvent(event);
        assertEquals(event, result);
    }

    @Test
    public void whenDeleteEventThenOneIsDeleted() {
        Event event = new EventImpl(22, "testEvent", new Date(), new BigDecimal(BigInteger.ONE));
        Event createdEvent = eventDao.createEvent(event);
        boolean result = eventDao.deleteEvent(createdEvent.getId());
        assertTrue(result);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();
}
