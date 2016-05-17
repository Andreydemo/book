package com.epam.cdp.preloading;

import com.epam.cdp.model.impl.TicketImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "tickets")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketsWrapper {
    @XmlElement(name = "ticket")
    List<TicketImpl> tickets;

    public List<TicketImpl> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    @Override
    public String toString() {
        return "TicketsWrapper{" +
                "tickets=" + tickets +
                '}';
    }
}
