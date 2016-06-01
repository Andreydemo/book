package com.epam.cdp.restClient.impl;

import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.EventImpl;
import com.epam.cdp.model.impl.TicketImpl;
import com.epam.cdp.model.impl.UserImpl;
import com.epam.cdp.restClient.TicketRestClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.epam.cdp.restClient.impl.Constants.EVENT_ID;
import static com.epam.cdp.restClient.impl.Constants.PAGE_SIZE_PAGE_NUM;
import static com.epam.cdp.restClient.impl.Constants.USER_ID;

@Component
public class TicketRestClientImpl implements TicketRestClient {
    private static final Logger logger = Logger.getLogger(TicketRestClientImpl.class);
    private static final String PDF = ".pdf";
    private RestTemplate restTemplate;
    private String baseUrl;
    private String fileName;

    @Autowired
    public TicketRestClientImpl(RestTemplate restTemplate, @Value("${url.tickets}") String baseUrl, @Value("${file.name}")String fileName) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.fileName = fileName;
    }

    @Override
    public Ticket bookTicket(Ticket ticket) {
        ResponseEntity<TicketImpl> ticketResponseEntity = restTemplate.postForEntity(baseUrl, ticket, TicketImpl.class);
        logger.debug("Booked ticket: " + ticketResponseEntity.getBody());
        return ticketResponseEntity.getBody();
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        List<Ticket> tickets = restTemplate.getForObject(baseUrl + USER_ID + PAGE_SIZE_PAGE_NUM, List.class, user.getId(), pageSize, pageNum);
        logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        List<Ticket> tickets = restTemplate.getForObject(baseUrl + EVENT_ID + PAGE_SIZE_PAGE_NUM, List.class, event.getId(), pageSize, pageNum);
        logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
        return tickets;
    }

    @Override
    public void cancelTicket(long ticketId) {
        logger.debug("Canceling ticket with id: " + ticketId);
        restTemplate.delete(baseUrl + "{id}", ticketId);
    }

    @Override
    public void getBookedTicketsByUserInPdf(long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(new MediaType("application", "pdf")));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(baseUrl + EVENT_ID + PDF, HttpMethod.GET, entity, byte[].class, id);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                Files.write(Paths.get(fileName), response.getBody());
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    @Override
    public void call() {
        TicketImpl ticket = new TicketImpl(1, 1, 1, Ticket.Category.STANDART, 22);
        Ticket bookedTicket = bookTicket(ticket);
        logger.debug(bookedTicket);
        getBookedTicketsByUserInPdf(ticket.getUserId());
        logger.debug(getBookedTickets(new UserImpl(bookedTicket.getUserId(), "name", "email"), 10, 1));
        logger.debug(getBookedTickets(new EventImpl(bookedTicket.getEventId(), "title", new Date(3232), new BigDecimal("1.00")), 10, 1));
        cancelTicket(bookedTicket.getId());
    }
}