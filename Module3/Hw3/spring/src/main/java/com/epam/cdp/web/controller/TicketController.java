package com.epam.cdp.web.controller;

import com.epam.cdp.facade.BookingFacade;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.Ticket;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.TicketImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * Controller that receives requests for ticket entity.
 */
@Controller
@RequestMapping("/tickets")
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
     *
     * @param ticket ticket to book.
     * @return Booked Ticket
     */
    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Ticket bookTicket(@RequestBody TicketImpl ticket) {
        logger.debug("Try to book ticket: " + ticket);
        Ticket bookedTicket = bookingFacade.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getPlace(), ticket.getCategory());
        logger.debug("Booked ticket: " + bookedTicket);
        return bookedTicket;
    }

    /**
     * Get all booked tickets for specified user. Tickets are sorted by event date in descending order.
     *
     * @param userId   id of user
     * @param pageSize Pagination param. Number of tickets to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return List of Tickets that were booked for specified user or empty collection if there is no tickets or user.
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Ticket> getTicketsByUser(@PathVariable long userId,
                                  @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                                  @RequestParam(defaultValue = "1") int pageNum) {
        User user = bookingFacade.getUserById(userId);
        if (user != null) {
            List<Ticket> tickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);
            logger.debug("Returning booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
            return tickets;
        }
        logger.debug("Such user does not exist, returning empty collection");
        return Collections.emptyList();
    }

    /**
     * Get all booked tickets for specified user. Tickets are sorted by event date in descending order.
     *
     * @param userId   id of user
     * @param pageSize Pagination param. Number of tickets to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return List of Tickets that were booked for specified user or empty collection if there is no tickets or user.
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, params = "visual=true")
    public String getTicketsByUser(@PathVariable long userId,
                                   @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                                   @RequestParam(defaultValue = "1") int pageNum,
                                   Model model) {
        User user = bookingFacade.getUserById(userId);
        if (user != null) {
            List<Ticket> tickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);
            model.addAttribute("tickets", tickets);
            logger.debug("Booked tickets by user: " + user + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
            return "ticket";
        }
        logger.debug("Such user does not exist, forwarding to error page");
        return "error";
    }

    /**
     * Get all booked tickets for specified event. Tickets are sorted by user email in ascending order.
     *
     * @param eventId  id of event
     * @param pageSize Pagination param. Number of tickets to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return List of Tickets that were booked for specified event or empty collection if there is no tickets or event.
     */
    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Ticket> getTicketsByEvent(@PathVariable long eventId,
                                   @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                                   @RequestParam(defaultValue = "1") int pageNum) {
        Event event = bookingFacade.getEventById(eventId);
        if (event != null) {
            List<Ticket> tickets = bookingFacade.getBookedTickets(event, pageSize, pageNum);
            logger.debug("Returning booked tickets by event: " + event + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + tickets);
            return tickets;
        }
        logger.debug("Such event does not exist, returning empty collection");
        return Collections.emptyList();
    }

    /**
     * Cancel ticket with a specified id.
     *
     * @param ticketId id of ticket to cancel.
     * @return boolean value whether ticket was canceled.
     */
    @RequestMapping(value = "/{ticketId}", method = RequestMethod.DELETE)
    public void cancelTicket(@PathVariable long ticketId, HttpServletResponse response) {
        logger.debug("Canceling ticket with id: " + ticketId);
        boolean canceled = bookingFacade.cancelTicket(ticketId);
        if (!canceled)
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}