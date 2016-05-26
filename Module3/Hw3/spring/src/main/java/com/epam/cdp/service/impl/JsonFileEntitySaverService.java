package com.epam.cdp.service.impl;

import com.epam.cdp.dao.EventDao;
import com.epam.cdp.dao.TicketDao;
import com.epam.cdp.dao.UserDao;
import com.epam.cdp.model.impl.EventImpl;
import com.epam.cdp.model.impl.TicketImpl;
import com.epam.cdp.model.impl.UserImpl;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Saves entities to storage from json file.
 */
@Service
public class JsonFileEntitySaverService implements com.epam.cdp.service.FileEntitySaverService {
    private static final Logger logger = Logger.getLogger(JsonFileEntitySaverService.class);
    private EventDao eventDao;
    private UserDao userDao;
    private TicketDao ticketDao;
    private Gson gson;

    @Autowired
    public JsonFileEntitySaverService(EventDao eventDao, UserDao userDao, TicketDao ticketDao, Gson gson) {
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.ticketDao = ticketDao;
        this.gson = gson;
    }

    @Override
    public void save(String filePath) {
        Map<String, JsonElement> elementMap = getStringJsonElementMap(filePath, gson);
        elementMap.entrySet().forEach(this::saveEntry);
    }

    private Map<String, JsonElement> getStringJsonElementMap(String filePath, Gson gson) {
        Type type = new TypeToken<Map<String, JsonElement>>() {}.getType();
        try {
            return gson.fromJson(new FileReader(filePath), type);
        } catch (FileNotFoundException e) {
            logger.error("Cannot read file, message: " + e.getMessage() + ". Returning empty map");
            return Collections.emptyMap();
        }
    }

    private void saveEntry(Map.Entry<String, JsonElement> entry) {
        if (entry.getKey().equals("events")) {
            List<EventImpl> list = getEntityList(entry.getValue(), new TypeToken<List<EventImpl>>() {});
            list.forEach(eventDao::createEvent);
        }
        if (entry.getKey().equals("users")) {
            List<UserImpl> list = getEntityList(entry.getValue(), new TypeToken<List<UserImpl>>() {});
            list.forEach(userDao::createUser);
        }
        if (entry.getKey().equals("tickets")) {
            List<TicketImpl> list = getEntityList(entry.getValue(), new TypeToken<List<TicketImpl>>() {});
            for (TicketImpl ticket : list)
                ticketDao.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getPlace(), ticket.getCategory());
        }
    }

    private <T> T getEntityList(JsonElement jsonEntity, TypeToken<T> typeToken) {
        return gson.fromJson(jsonEntity, typeToken.getType());
    }
}