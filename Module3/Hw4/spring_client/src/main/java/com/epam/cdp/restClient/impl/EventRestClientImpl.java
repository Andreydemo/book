package com.epam.cdp.restClient.impl;

import com.epam.cdp.model.Event;
import com.epam.cdp.model.impl.EventImpl;
import com.epam.cdp.restClient.EventRestClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.epam.cdp.restClient.impl.Constants.PAGE_SIZE_PAGE_NUM;
import static com.epam.cdp.restClient.impl.Constants.TITLE;
import static com.epam.cdp.restClient.impl.Constants.DAY;

@Component
public class EventRestClientImpl implements EventRestClient {
    private static final Logger logger = Logger.getLogger(EventRestClientImpl.class);
    private RestTemplate restTemplate;
    private String baseUrl;
    private String datePattern;

    @Autowired
    public EventRestClientImpl(RestTemplate restTemplate, @Value("${url.events}") String baseUrl, @Value("${event.datePattern}") String datePattern) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.datePattern = datePattern;
    }

    @Override
    public Event getEventById(long eventId) {
        Event event = restTemplate.getForObject(baseUrl + "{id}", EventImpl.class, eventId);
        logger.debug("Returning event by id: " + eventId + ", " + event);
        return event;
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        List<Event> events = restTemplate.getForObject(baseUrl + TITLE + PAGE_SIZE_PAGE_NUM, List.class, title, pageSize, pageNum);
        logger.debug("Returning events by title: " + title + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        List<Event> events = restTemplate.getForObject(baseUrl + DAY + PAGE_SIZE_PAGE_NUM, List.class, simpleDateFormat.format(day), pageSize, pageNum);
        logger.debug("Returning events for day: " + day + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + events);
        return events;
    }

    @Override
    public Event createEvent(Event event) {
        ResponseEntity<EventImpl> entity = restTemplate.postForEntity(baseUrl, event, EventImpl.class);
        logger.debug("Creating event: " + event);
        return entity.getBody();
    }

    @Override
    public void updateEvent(Event event) {
        logger.debug("Updating event: " + event);
        restTemplate.put(baseUrl, event, EventImpl.class);
    }

    @Override
    public void deleteEvent(long eventId) {
        logger.debug("Deleting event with id: " + eventId);
        restTemplate.delete(baseUrl + "{id}", eventId);
    }

    @Override
    public void call() {
        Event event = createEvent(new EventImpl("title", new Date(1155555), new BigDecimal("1.00")));
        logger.debug(event);
        logger.debug(getEventById(event.getId()));
        logger.debug(getEventsByTitle(event.getTitle(), 10, 1));
        logger.debug(getEventsForDay(event.getDate(), 10, 1));
        updateEvent(new EventImpl(event.getId(), "new title", new Date(91919) ,new BigDecimal("20.00")));
        deleteEvent(event.getId());
    }
}