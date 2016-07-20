package com.epam.cdp.batulin;

import com.mongodb.BasicDBObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JmsNoteListener {
    @Autowired
    private MongoDbFactory mongo;

    @JmsListener(destination = "noteQueue")
    public void processMessage(Map<String,Object> map) {
        mongo.getDb().getCollection("archive").save(new BasicDBObject(map));
    }

    public void setMongo(MongoDbFactory mongo) {
        this.mongo = mongo;
    }
}
