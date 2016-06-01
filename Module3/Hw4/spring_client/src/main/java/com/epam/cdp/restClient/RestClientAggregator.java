package com.epam.cdp.restClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RestClientAggregator {
    private EventRestClient eventRestClient;
    private UserRestClient userRestClient;
    private TicketRestClient ticketRestClient;

    @Autowired
    public RestClientAggregator(EventRestClient eventRestClient, UserRestClient userRestClient, TicketRestClient ticketRestClient) {
        this.eventRestClient = eventRestClient;
        this.userRestClient = userRestClient;
        this.ticketRestClient = ticketRestClient;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/context.xml");
        RestClientAggregator client = context.getBean(RestClientAggregator.class);
        client.eventRestClient.call();
        client.ticketRestClient.call();
        client.userRestClient.call();
    }
}