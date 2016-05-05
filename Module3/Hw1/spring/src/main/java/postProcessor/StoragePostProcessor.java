package postProcessor;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import postProcessor.filler.impl.EventsStorageJsonFiller;
import postProcessor.filler.impl.TicketsStorageJsonFiller;
import postProcessor.filler.impl.UsersStorageJsonFiller;
import storage.Storage;

public class StoragePostProcessor implements BeanPostProcessor {
    private static final Logger logger = Logger.getLogger(StoragePostProcessor.class);
    private EventsStorageJsonFiller eventsJsonParser;
    private UsersStorageJsonFiller usersJsonParser;
    private TicketsStorageJsonFiller ticketsJsonParser;

    public StoragePostProcessor(EventsStorageJsonFiller eventsJsonParser, UsersStorageJsonFiller usersJsonParser, TicketsStorageJsonFiller ticketsJsonParser) {
        this.eventsJsonParser = eventsJsonParser;
        this.usersJsonParser = usersJsonParser;
        this.ticketsJsonParser = ticketsJsonParser;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.debug("Post process started");
        if (bean instanceof Storage) {
            eventsJsonParser.fill();
            usersJsonParser.fill();
            ticketsJsonParser.fill();
        }
        logger.debug("Post process finished");
        return bean;
    }
}