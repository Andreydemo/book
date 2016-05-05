package service;

import dao.EventDao;
import model.Event;
import model.impl.EventImpl;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import service.impl.EventServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventServiceTest {
    private EventService eventService;

    @Mock
    EventDao eventDao;

    @Before
    public void setUp() {
        eventDao = mock(EventDao.class);
        eventService = new EventServiceImpl();
        eventService.setEventDao(eventDao);
    }

    @Test
    public void whenCreateTestTheOneIsCreated() {
        Event event = new EventImpl(22, "testEvent", new Date());
        when(eventDao.createEvent(anyObject())).thenReturn(event);
        Event result = eventService.createEvent(event);
        assertEquals(event, result);
    }

    @Test
    public void whenDeleteEventTheOneIsDeleted() {
        when(eventDao.deleteEvent(anyLong())).thenReturn(true);
        boolean result = eventService.deleteEvent(22);
        assertTrue(result);
    }

    @Test
    public void whenGetEventByIdTheEventIsReturned() {
        Event event = new EventImpl(22, "testEvent", new Date());
        when(eventDao.getEventById(anyLong())).thenReturn(event);
        Event result = eventService.getEventById(22);
        assertEquals(event, result);
    }

    @Test
    public void whenUpdateTestTheOneIsUpdated() {
        Event event = new EventImpl(22, "testEvent", new Date());
        when(eventDao.updateEvent(anyObject())).thenReturn(event);
        Event result = eventService.updateEvent(event);
        assertEquals(event, result);
    }

    @Test
    public void whenGetEventsByTitleThenEventsAreReturned() {
        Event event = new EventImpl(2, "event", new Date(9999));
        when(eventDao.getEventsByTitle(anyString(), anyInt(), anyInt())).thenReturn(Arrays.asList(event));
        assertEquals(event.getTitle(), eventService.getEventsByTitle("event", 1, 1).get(0).getTitle());
    }

    @Test
    public void whenGetEventsForDayThenEventsAreReturned() {
        Event event = new EventImpl(2, "event", new Date(9999));
        when(eventDao.getEventsForDay(anyObject(), anyInt(), anyInt())).thenReturn(Arrays.asList(event));
        List<Event> events = eventService.getEventsForDay(new Date(9999), 1, 1);
        assertTrue(DateUtils.isSameDay(event.getDate(), events.get(0).getDate()));
    }
}
