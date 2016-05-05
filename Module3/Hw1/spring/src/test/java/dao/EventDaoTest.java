package dao;

import dao.EventDao;
import dao.impl.EventDaoImpl;
import model.Event;
import model.impl.EventImpl;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import storage.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventDaoTest {
    private EventDao eventDao;

    @Mock
    Storage storage;

    @Before
    public void setUp() {
        storage = mock(Storage.class);
        eventDao = new EventDaoImpl();
        eventDao.setStorage(storage);
    }

    @Test
    public void whenGetEventWithIdThenEventIsReturned() {
        Event event = new EventImpl(1, "", new Date(9999));
        when(storage.getEntityById(anyString())).thenReturn(event);
        assertEquals(event, eventDao.getEventById(1));
    }

    @Test
    public void whenGetEventsByTitleThenEventsAreReturned() {
        List<Object> events = new ArrayList<>();
        events.add(new EventImpl(2, "event", new Date(9999)));
        when(storage.getElementsByPredicate(anyString(), anyObject(), anyObject(), anyInt(), anyInt())).thenReturn(events);
        assertEquals(((Event) events.get(0)).getTitle(), eventDao.getEventsByTitle("event", 1, 1).get(0).getTitle());
    }

    @Test
    public void whenGetEventsByTitleWithPageSizeThenNumberOfEventsAreReturned() {
        List<Object> events = new ArrayList<>();
        events.add(new EventImpl(2, "event", new Date(9999)));
        events.add(new EventImpl(3, "event", new Date(9999)));
        when(storage.getElementsByPredicate(anyString(), anyObject(), anyObject(), anyInt(), anyInt())).thenReturn(events);
        List<Event> result = eventDao.getEventsByTitle("event", 2, 1);
        assertEquals(((Event) events.get(0)).getTitle(), result.get(0).getTitle());
        assertEquals(((Event) events.get(1)).getTitle(), result.get(1).getTitle());
    }

    @Test
    public void whenGetEventsByTitleWithHigherPageSizeThenNumberOfActualEventsAreReturned() {
        List<Object> events = new ArrayList<>();
        events.add(new EventImpl(2, "event", new Date(9999)));
        events.add(new EventImpl(3, "event", new Date(9999)));
        events.add(new EventImpl(4, "event", new Date(9999)));
        when(storage.getElementsByPredicate(anyString(), anyObject(), anyObject(), anyInt(), anyInt())).thenReturn(events);
        List<Event> result = eventDao.getEventsByTitle("event", 20, 1);
        assertEquals(((Event) events.get(0)).getTitle(), result.get(0).getTitle());
        assertEquals(((Event) events.get(1)).getTitle(), result.get(1).getTitle());
        assertEquals(((Event) events.get(2)).getTitle(), result.get(2).getTitle());
        assertEquals(3, result.size());
    }

    @Test
    public void whenGetEventsByTitleSecondPageThenSecondPageIsReturned() {
        List<Object> events = new ArrayList<>();
        events.add(new EventImpl(2, "event", new Date(9999)));
        when(storage.getElementsByPredicate(anyString(), anyObject(), anyObject(), anyInt(), anyInt())).thenReturn(events);
        List<Event> result = eventDao.getEventsByTitle("event", 2, 2);
        assertEquals(((Event) events.get(0)).getTitle(), result.get(0).getTitle());
        assertEquals(1, result.size());
    }

    @Test
    public void whenGetEventsForDayWithOnePageSizeThenOneEventIsReturned() {
        Event expected = new EventImpl(2, "event", new Date(9999));
        when(storage.getElementsByPredicate(anyString(), anyObject(), anyObject(), anyInt(), anyInt())).thenReturn(Arrays.asList(expected));
        List<Event> events = eventDao.getEventsForDay(new Date(9999), 1, 1);
        assertTrue(DateUtils.isSameDay(expected.getDate(), events.get(0).getDate()));
    }

    @Test
    public void whenCreateEventThenOneIsCreated() {
        Event event = new EventImpl(22, "testEvent", new Date());
        when(storage.put(anyString(), anyObject())).thenReturn(event);
        Event result = eventDao.createEvent(event);
        assertEquals(event, result);
    }

    @Test
    public void whenUpdateEventThenOneIsUpdated() {
        Event event = new EventImpl(22, "testEvent", new Date());
        eventDao.createEvent(event);
        event = new EventImpl(22, "EVENT", new Date(2222));
        when(storage.put(anyString(), eq(event))).thenReturn(event);
        Event result = eventDao.updateEvent(event);
        assertEquals(event, result);
    }

    @Test
    public void whenDeleteEventThenOneIsDeleted() {
        Event event = new EventImpl(22, "testEvent", new Date());
        when(storage.put(anyString(), anyObject())).thenReturn(event);
        eventDao.createEvent(event);
        when(storage.delete(anyString())).thenReturn(true);
        boolean result = eventDao.deleteEvent(event.getId());
        assertTrue(result);
    }
}
