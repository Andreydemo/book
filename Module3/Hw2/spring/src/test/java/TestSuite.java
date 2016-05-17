import com.epam.cdp.dao.EventDaoTest;
import com.epam.cdp.dao.TicketDaoTest;
import com.epam.cdp.dao.UserAccountDaoTest;
import com.epam.cdp.dao.UserDaoTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.epam.cdp.service.EventServiceTest;
import com.epam.cdp.service.TicketServiceTest;
import com.epam.cdp.service.UserServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserAccountDaoTest.class,
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