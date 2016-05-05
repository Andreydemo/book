import dao.EventDaoTest;
import dao.TicketDaoTest;
import dao.UserDaoTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import service.EventServiceTest;
import service.TicketServiceTest;
import service.UserServiceTest;
import storage.StorageTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StorageTest.class,
        EventDaoTest.class,
        TicketDaoTest.class,
        UserDaoTest.class,
        EventServiceTest.class,
        TicketServiceTest.class,
        UserServiceTest.class,
        IntegrationTest.class
})
public class TestSuite {
} 