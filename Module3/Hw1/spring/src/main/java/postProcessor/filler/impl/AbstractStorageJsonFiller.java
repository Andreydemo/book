package postProcessor.filler.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import postProcessor.filler.StorageFiller;
import storage.Storage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.List;

public abstract class AbstractStorageJsonFiller<T> implements StorageFiller<T> {
    protected Storage storage;
    private String filePath;
    protected static final Logger logger = Logger.getLogger(StorageFiller.class);

    public AbstractStorageJsonFiller(Storage storage, String filePath) {
        this.storage = storage;
        this.filePath = filePath;
    }

    @Override
    public void fill() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        Gson gson = builder.create();
        try {
            List<? extends T> events = gson.fromJson(new FileReader(filePath), new TypeToken<List<T>>() {
            }.getType());
            events.forEach(this::put);
        } catch (FileNotFoundException e) {
            logger.error("Cannot find file with path: " + filePath);
        }
    }

    protected abstract void put(T element);
}
