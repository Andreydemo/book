package com.epam.cdp.model.impl;

import com.epam.cdp.model.Ticket;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;
import java.util.Objects;

public class TicketImpl implements Ticket, Serializable {
    private long id;
    private long eventId;
    private long userId;
    private Category category;
    private int place;

    public TicketImpl(long id, long eventId, long userId, Category category, int place) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.category = category;
        this.place = place;
    }

    public TicketImpl() {
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getEventId() {
        return eventId;
    }

    @XmlAttribute(name="event")
    @Override
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @XmlAttribute(name="user")
    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @XmlAttribute(name="category")
    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int getPlace() {
        return place;
    }

    @XmlAttribute(name="place")
    @Override
    public void setPlace(int place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketImpl ticket = (TicketImpl) o;
        return id == ticket.id &&
                eventId == ticket.eventId &&
                userId == ticket.userId &&
                place == ticket.place &&
                category == ticket.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId, userId, category, place);
    }

    @Override
    public String toString() {
        return "TicketImpl{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", category=" + category +
                ", place=" + place +
                '}';
    }
}
