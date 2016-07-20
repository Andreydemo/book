package com.epam.cdp.batulin.batulin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "discovery");
        SpringApplication.run(DiscoveryApplication.class, args);
    }
}
