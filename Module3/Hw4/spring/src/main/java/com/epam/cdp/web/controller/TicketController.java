package com.epam.cdp.web.controller;

import com.epam.cdp.exception.EntityNotFoundException;
import com.epam.cdp.exception.TicketNotBookedException;
import com.epam.cdp.facade.BookingFacade;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.TicketImpl;
import com.epam.cdp.web.Error;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("tickets")
public class TicketController {
    private static final Logger logger = Logger.getLogger(TicketController.class);
    private static final String MAX_INT_AS_STRING = "2147483647";
    private BookingFacade bookingFacade;

    @Autowired
    public TicketController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    /**
     * Book ticket for a specified event on behalf of specified user.
     * If such user or event does not exist or place is already booked or there is no money on user's account then no ticket would be booked.
     * If the ticket is booked than response status is 201 (created). Otherwise, response status will be  400 (bad request).
     *
     * @param ticket ticket to book.
     * @return Booked Ticket.
     * @throws com.epam.cdp.exception.TicketNotBookedException if params are not correct.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Ticket> bookTicket(@RequestBody TicketImpl ticket) {
        logger.debug("Try to book ticket: " + ticket);
        Ticket bookedTicket = bookingFacade.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getPlace(), ticket.getCategory());
        logger.debug("Booked ticket: " + bookedTicket);

        if (bookedTicket == null)
            throw new TicketNotBookedException("Unable to book ticket, input parameters are not correct");

        return getResourceWithEventAndUserLinks(ticket);
    }

    /**
     * Get all booked tickets for specified user. Tickets are sorted by event date in descending order.
     *
     * @param userId   id of user.
     * @param pageSize Pagination param. Number of tickets to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return List of Tickets that were booked for specified user or empty collection if there is no tickets or user.
     * @throws com.epam.cdp.exception.EntityNotFoundException if there is no such user or any matching tickets.
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<Resource<Ticket>> getTicketsByUser(@PathVariable long userId,
                                                   @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                                                   @RequestParam(defaultValue = "1") int pageNum) {
        User user = bookingFacade.getUserById(userId);
        if (user == null)
            throw new EntityNotFoundException("user", userId);
        List<Ticket> tickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);
        throwExceptionIfTicketListIsEmpty(tickets);
        logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);

        return tickets.stream().map(this::getResourceWithEventAndUserLinks).collect(Collectors.toList());
    }

    /**
     * Get all booked tickets for specified event. Tickets are sorted by user email in ascending order.
     *
     * @param eventId  id of event
     * @param pageSize Pagination param. Number of tickets to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return List of Tickets that were booked for specified event or empty collection if there is no tickets or event.
     * @throws com.epam.cdp.exception.EntityNotFoundException if there is no such event or any matching tickets.
     */
    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.GET)
    public List<Resource<Ticket>> getTicketsByEvent(@PathVariable long eventId,
                                                    @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                                                    @RequestParam(defaultValue = "1") int pageNum) {
        Event event = bookingFacade.getEventById(eventId);
        if (event == null) {
            throw new EntityNotFoundException("event", eventId);
        }
        List<Ticket> tickets = bookingFacade.getBookedTickets(event, pageSize, pageNum);
        throwExceptionIfTicketListIsEmpty(tickets);
        logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);

        return tickets.stream().map(this::getResourceWithEventAndUserLinks).collect(Collectors.toList());
    }

    /**
     * Cancel ticket with a specified id.
     * If the ticket was successfully canceled than response status will be set to 204 (no content). Otherwise, it will be 404 (not found).
     *
     * @param ticketId id of ticket to cancel.
     */
    @RequestMapping(value = "/{ticketId}", method = RequestMethod.DELETE)
    public void cancelTicket(@PathVariable long ticketId, HttpServletResponse response) {
        logger.debug("Canceling ticket with id: " + ticketId);
        boolean canceled = bookingFacade.cancelTicket(ticketId);

        if (canceled)
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        else
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        logger.debug("Ticket with id = " + ticketId + " was canceled: " + canceled + ". Response status is set to :" + response.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error entityNotFound(EntityNotFoundException e) {
        if (e.getEntityId() != null) {
            return new Error(404, "Entity " + e.getMessage() + ":" + e.getEntityId() + " not found");
        }
        return new Error(404, "Entities of type " + e.getMessage() + " not found for request");
    }

    @ExceptionHandler({
            TicketNotBookedException.class,
            IllegalStateException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error ticketNotBooked(Exception e) {
        return new Error(400, e.getMessage());
    }

    private void throwExceptionIfTicketListIsEmpty(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            logger.debug("Ticket list is empty throwing EntityNotFoundException");
            throw new EntityNotFoundException("ticket");
        }
    }

    private Resource<Ticket> getResourceWithEventAndUserLinks(Ticket ticket) {
        Resource<Ticket> resource = new Resource<>(ticket);
        resource.add(linkTo(methodOn(EventController.class).getEvent(ticket.getEventId())).withRel("event"));
        resource.add(linkTo(methodOn(UserController.class).getUserById(ticket.getUserId())).withRel("user"));
        try {
            Link link = linkTo(
                    TicketController.class,
                    TicketController.class.getMethod("cancelTicket", long.class, HttpServletResponse.class),
                    ticket.getId()
            ).withRel("cancel");
            resource.add(link);
        } catch (NoSuchMethodException e) {
            logger.warn(e.getMessage());
        }
        return resource;
    }
}