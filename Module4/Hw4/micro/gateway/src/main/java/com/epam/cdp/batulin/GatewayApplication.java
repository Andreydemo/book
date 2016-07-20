package com.epam.cdp.batulin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class GatewayApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "gateway");
        SpringApplication.run(GatewayApplication.class, args);
    }
}
