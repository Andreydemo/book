package com.epam.cdp.restClient;

import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;

import java.util.List;

public interface TicketRestClient {
    Ticket bookTicket(Ticket ticket);

    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    void cancelTicket(long ticketId);

    void getBookedTicketsByUserInPdf(long id);

    void call();
}
