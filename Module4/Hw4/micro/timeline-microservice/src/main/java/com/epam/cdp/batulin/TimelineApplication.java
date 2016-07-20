package com.epam.cdp.batulin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJms
public class TimelineApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "timeline");
        SpringApplication.run(TimelineApplication.class, args);
    }
}

