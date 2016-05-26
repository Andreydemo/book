package com.epam.cdp.web.controller;

import com.epam.cdp.facade.BookingFacade;
import com.epam.cdp.model.Event;
import com.epam.cdp.model.impl.EventImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Controller that receives requests for event entity.
 */
@Controller
@RequestMapping("/events")
public class EventController {
    private static final Logger logger = Logger.getLogger(EventController.class);
    private BookingFacade bookingFacade;
    private static final String MAX_INT_AS_STRING = "2147483647";

    @Autowired
    public EventController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    /**
     * Gets event by id.
     *
     * @param eventId id of event.
     * @return Event object for given id or null if there is no event.
     */
    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Event getEvent(@PathVariable long eventId) {
        Event event = bookingFacade.getEventById(eventId);
        logger.debug("Returning event by id: " + eventId + ", " + event);
        return event;
    }

    /**
     * Get list of events by matching title. Title is matched using 'contains' approach.
     *
     * @param title    Event title or it's part.
     * @param pageSize Pagination param. Number of events to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return List of events for given title of empty list if there is no events.
     */
    @RequestMapping(value = "/title/{title}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Event> getEventsByTitle(@PathVariable String title,
                                 @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                                 @RequestParam(defaultValue = "1") int pageNum) {
        List<Event> events = bookingFacade.getEventsByTitle(title, pageSize, pageNum);
        logger.debug("Returning events by title: " + title + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    /**
     * Get list of events by matching title. Title is matched using 'contains' approach.
     *
     * @param title    Event title or it's part.
     * @param pageSize Pagination param. Number of events to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return Name of the view.
     */
    @RequestMapping(value = "/title/{title}", method = RequestMethod.GET, params = "visual=true")
    public String getEventsByTitle(@PathVariable String title,
                                   @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                                   @RequestParam(defaultValue = "1") int pageNum,
                                   Model model) {
        List<Event> events = bookingFacade.getEventsByTitle(title, pageSize, pageNum);
        logger.debug("events by title: " + title + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        model.addAttribute("attribute", events);
        return "representation";
    }

    /**
     * Get list of events for specified day.
     *
     * @param day      Date object from which day information is extracted. Format "yyyy-MM-dd"
     * @param pageSize Pagination param. Number of events to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return List of events for given day of empty list if there is no events.
     */
    @RequestMapping(value = "/day/{day}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Event> getEventsForDay(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date day,
                                @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                                @RequestParam(defaultValue = "1") int pageNum) {
        List<Event> events = bookingFacade.getEventsForDay(day, pageSize, pageNum);
        logger.debug("Returning events for day: " + day + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    /**
     * Creates new event. Id is auto-generated.
     *
     * @param event Event to create.
     * @return Created Event.
     */
    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Event createEvent(@RequestBody EventImpl event) {
        logger.debug("Creating event: " + event);
        return bookingFacade.createEvent(event);
    }

    /**
     * Updates event using given data.
     *
     * @param event event to update.
     * @return Updated Event
     */
    @RequestMapping(method = RequestMethod.PUT)
    public
    @ResponseBody
    Event updateEvent(@RequestBody EventImpl event) {
        logger.debug("Updating event: " + event);
        return bookingFacade.updateEvent(event);
    }

    /**
     * Deletes event by its id.
     *
     * @param eventId id of event to delete.
     * @return Whether event was deleted.
     */
    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    public void deleteEvent(@PathVariable long eventId, HttpServletResponse response) {
        logger.debug("Deleting event with id: " + eventId);
        boolean deleted = bookingFacade.deleteEvent(eventId);
        if (!deleted)
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}