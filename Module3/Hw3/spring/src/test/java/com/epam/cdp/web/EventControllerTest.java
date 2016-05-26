package com.epam.cdp.web;

import com.epam.cdp.facade.BookingFacade;
import com.epam.cdp.model.impl.EventImpl;
import com.epam.cdp.web.controller.EventController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.epam.cdp.TestUtils.APPLICATION_JSON_UTF8;
import static com.epam.cdp.TestUtils.convertObjectToJsonBytes;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest {
    @Mock
    BookingFacade facade;

    EventController eventController;

    @Before
    public void setUp() {
        facade = mock(BookingFacade.class);
        eventController = new EventController(facade);
    }

    @Test
    public void whenGetEventByIdThenEventIsReturnedInJson() throws Exception {
        EventImpl event = new EventImpl(1, "", new Date(222), new BigDecimal("1.22"));
        when(facade.getEventById(anyLong())).thenReturn(event);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        mockMvc.perform(get("/events/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(event.getTitle())))
                .andExpect(jsonPath("$.date", is(222)))
                .andExpect(jsonPath("$.ticketPrice", is(event.getTicketPrice().doubleValue())));

        verify(facade, times(1)).getEventById(anyLong());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenGetEventsByTitleThenEventsAreReturnedInJson() throws Exception {
        String title = "title";
        EventImpl event = new EventImpl(1, title, new Date(222), new BigDecimal("1.22"));
        EventImpl event1 = new EventImpl(2, title, new Date(333), new BigDecimal("2.33"));
        when(facade.getEventsByTitle(anyString(), anyInt(), anyInt())).thenReturn(Arrays.asList(event, event1));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        mockMvc.perform(get("/events/title/{title}", event.getTitle()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].title", is(event.getTitle())))
                .andExpect(jsonPath("$[1].title", is(event1.getTitle())));

        verify(facade, times(1)).getEventsByTitle(anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenGetEventsForDayThenEventsAreReturnedInJson() throws Exception {
        Date date = new Date(333);
        EventImpl event = new EventImpl(1, "", date, new BigDecimal("1.22"));
        EventImpl event1 = new EventImpl(2, "xx", date, new BigDecimal("2.33"));
        when(facade.getEventsForDay(anyObject(), anyInt(), anyInt())).thenReturn(Arrays.asList(event, event1));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        mockMvc.perform(get("/events/day/{day}", simpleDateFormat.format(event.getDate())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].date", is(333)))
                .andExpect(jsonPath("$[1].date", is(333)));

        verify(facade, times(1)).getEventsForDay(anyObject(), anyInt(), anyInt());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenCreateEventThenEventIsCreatedAndReturnedInJson() throws Exception {
        EventImpl event = new EventImpl(1, "title", new Date(333), new BigDecimal("1.22"));
        when(facade.createEvent(anyObject())).thenReturn(event);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        mockMvc.perform(post("/events/").
                content(convertObjectToJsonBytes(event)).
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(event.getTitle())))
                .andExpect(jsonPath("$.date", is(333)))
                .andExpect(jsonPath("$.ticketPrice", is(event.getTicketPrice().doubleValue())));

        verify(facade, times(1)).createEvent(anyObject());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenUpdateEventsThenEventIsUpdatedAndReturnedInJson() throws Exception {
        EventImpl event = new EventImpl(1, "title", new Date(333), new BigDecimal("1.22"));
        when(facade.updateEvent(anyObject())).thenReturn(event);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        mockMvc.perform(put("/events/").
                content(convertObjectToJsonBytes(event)).
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(event.getTitle())))
                .andExpect(jsonPath("$.date", is(333)))
                .andExpect(jsonPath("$.ticketPrice", is(event.getTicketPrice().doubleValue())));

        verify(facade, times(1)).updateEvent(anyObject());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenDeleteEventThenEventIsDeletedAndStatusIs200() throws Exception {
        when(facade.deleteEvent(anyLong())).thenReturn(true);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        mockMvc.perform(delete("/events/1").
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk());

        verify(facade, times(1)).deleteEvent(anyLong());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenDeleteEventAndThereIsNoEventThenStatusNoFoundIsSent() throws Exception {
        when(facade.deleteEvent(anyLong())).thenReturn(false);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        mockMvc.perform(delete("/events/1").
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isNotFound());

        verify(facade, times(1)).deleteEvent(anyLong());
        verifyNoMoreInteractions(facade);
    }
}