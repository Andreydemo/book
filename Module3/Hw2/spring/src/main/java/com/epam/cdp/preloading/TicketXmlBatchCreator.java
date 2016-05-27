package com.epam.cdp.preloading;

import com.epam.cdp.dao.TicketDao;
import com.epam.cdp.model.impl.TicketImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.util.List;

@Component
public class TicketXmlBatchCreator implements TicketBatchCreator {
    private static final Logger logger = Logger.getLogger(TicketXmlBatchCreator.class);
    private String filePath;
    private TicketDao ticketDao;
    private Unmarshaller unmarshaller;
    private TransactionTemplate transactionTemplate;

    @Autowired
    public TicketXmlBatchCreator(@Value("${file.ticketsFilePath}")
                                         String filePath,
                                 TicketDao ticketDao,
                                 Unmarshaller unmarshaller,
                                 TransactionTemplate transactionTemplate) {
        this.filePath = filePath;
        this.ticketDao = ticketDao;
        this.unmarshaller = unmarshaller;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void create() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                performTransaction(status);
            }
        });
    }

    private void performTransaction(TransactionStatus status) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(filePath);
            logger.debug("Unmarshalling tickets from " + filePath);
            TicketsWrapper ticketsWrapper = (TicketsWrapper) unmarshaller.unmarshal(new StreamSource(fileInputStream));
            writeToDatabase(ticketsWrapper);
        } catch (Exception e) {
            logger.error("Cannot create tickets from xml, message: " + e.getMessage());
            status.setRollbackOnly();
        }
    }

    private void writeToDatabase(TicketsWrapper ticketsWrapper) {
        List<TicketImpl> tickets = ticketsWrapper.getTickets();
        ticketDao.insertBatch(tickets);
    }
}