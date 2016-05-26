package com.epam.cdp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;

@Configuration
@ComponentScan(basePackages = {"com.epam.cdp"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
        })
@Import(PersistenceConfig.class)
public class RootConfig {

    static
    @Bean
    public PropertySourcesPlaceholderConfigurer myPropertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
        Resource[] resourceLocations = new Resource[]{
                new ClassPathResource("properties.properties"),
        };
        p.setLocations(resourceLocations);
        return p;
    }

    @Bean
    Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class,
                        (JsonDeserializer<Date>) (json, typeOfT, context) ->
                                new Date(json.getAsJsonPrimitive().getAsLong()))
                .create();
    }
}