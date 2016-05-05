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
    private EventsStorageJsonFiller eventsStorageJsonFiller;
    private UsersStorageJsonFiller usersStorageJsonFiller;
    private TicketsStorageJsonFiller ticketsStorageJsonFiller;

    public StoragePostProcessor(EventsStorageJsonFiller eventsStorageJsonFiller, UsersStorageJsonFiller usersStorageJsonFiller, TicketsStorageJsonFiller ticketsStorageJsonFiller) {
        this.eventsStorageJsonFiller = eventsStorageJsonFiller;
        this.usersStorageJsonFiller = usersStorageJsonFiller;
        this.ticketsStorageJsonFiller = ticketsStorageJsonFiller;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.debug("Post process started");
        if (bean instanceof Storage) {
            eventsStorageJsonFiller.fill();
            usersStorageJsonFiller.fill();
            ticketsStorageJsonFiller.fill();
        }
        logger.debug("Post process finished");
        return bean;
    }
}