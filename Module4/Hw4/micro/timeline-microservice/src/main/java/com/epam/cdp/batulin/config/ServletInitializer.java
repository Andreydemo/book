package com.epam.cdp.batulin.config;

import com.epam.cdp.batulin.TimelineApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TimelineApplication.class);
    }
}
