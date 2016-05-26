package com.epam.cdp.web;

import com.epam.cdp.facade.BookingFacade;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.EventImpl;
import com.epam.cdp.model.impl.TicketImpl;
import com.epam.cdp.model.impl.UserImpl;
import com.epam.cdp.web.controller.TicketController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import static com.epam.cdp.TestUtils.APPLICATION_JSON_UTF8;
import static com.epam.cdp.TestUtils.convertObjectToJsonBytes;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TicketControllerTest {
    @Mock
    BookingFacade facade;

    TicketController ticketController;

    @Before
    public void setUp() {
        facade = mock(BookingFacade.class);
        ticketController = new TicketController(facade);
    }

    @Test
    public void whenBookTicketThenBookedTicketIsReturnedInJson() throws Exception {
        int id = 1;
        Ticket ticket = new TicketImpl(id, id, id, Ticket.Category.BAR, id);
        when(facade.bookTicket(anyLong(), anyLong(), anyInt(), anyObject())).thenReturn(ticket);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();

        mockMvc.perform(post("/tickets/").
                content(convertObjectToJsonBytes(ticket)).
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.eventId", is(id)))
                .andExpect(jsonPath("$.userId", is(id)))
                .andExpect(jsonPath("$.category", is(ticket.getCategory().toString())))
                .andExpect(jsonPath("$.place", is(ticket.getPlace())));

        verify(facade, times(id)).bookTicket(anyLong(), anyLong(), anyInt(), anyObject());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenGetBookedTicketsByUserThenListOfEventsIsReturnedInJson() throws Exception {
        Ticket ticket = new TicketImpl(1, 1, 1, Ticket.Category.BAR, 1);
        Ticket ticket1 = new TicketImpl(1, 2, 1, Ticket.Category.PREMIUM, 2);
        when(facade.getBookedTickets(any(User.class), anyInt(), anyInt())).thenReturn(Arrays.asList(ticket, ticket1));
        when(facade.getUserById(anyLong())).thenReturn(new UserImpl(1, "", ""));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();

        mockMvc.perform(get("/tickets/user/{id}", ticket.getUserId()).
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].userId", is((int)ticket.getUserId())))
                .andExpect(jsonPath("$[1].userId", is((int)ticket1.getUserId())));

        verify(facade, times(1)).getBookedTickets(any(User.class), anyInt(), anyInt());
    }

    @Test
    public void whenGetBookedTicketsByEventThenListOfEventsIsReturnedInJson() throws Exception {
        Ticket ticket = new TicketImpl(1, 1, 1, Ticket.Category.BAR, 1);
        Ticket ticket1 = new TicketImpl(1, 1, 2, Ticket.Category.PREMIUM, 2);
        when(facade.getBookedTickets(any(Event.class), anyInt(), anyInt())).thenReturn(Arrays.asList(ticket, ticket1));
        when(facade.getEventById(anyLong())).thenReturn(new EventImpl(1, "", new Date(), new BigDecimal("1.00")));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();

        mockMvc.perform(get("/tickets/event/{id}", ticket.getEventId()).
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].eventId", is((int)ticket.getEventId())))
                .andExpect(jsonPath("$[1].eventId", is((int)ticket1.getEventId())));

        verify(facade, times(1)).getBookedTickets(any(Event.class), anyInt(), anyInt());
    }

    @Test
    public void whenCancelTicketThenTicketIsCanceledAndStatusIs200() throws Exception {
        when(facade.cancelTicket(anyLong())).thenReturn(true);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();

        mockMvc.perform(delete("/tickets/1").
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk());

        verify(facade, times(1)).cancelTicket(anyLong());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenDeleteEventAndThereIsNoEventThenStatusNoFoundIsSent() throws Exception {
        when(facade.cancelTicket(anyLong())).thenReturn(false);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();

        mockMvc.perform(delete("/tickets/1").
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isNotFound());

        verify(facade, times(1)).cancelTicket(anyLong());
        verifyNoMoreInteractions(facade);
    }
}