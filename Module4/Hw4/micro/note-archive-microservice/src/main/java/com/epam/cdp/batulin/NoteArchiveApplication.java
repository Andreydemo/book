package com.epam.cdp.batulin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class, WebMvcAutoConfiguration.class})
@EnableJms
public class NoteArchiveApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "archive");
        SpringApplication.run(NoteArchiveApplication.class, args);
    }
}

