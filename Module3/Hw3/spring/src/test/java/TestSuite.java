import com.epam.cdp.IntegrationTest;
import com.epam.cdp.dao.EventDaoTest;
import com.epam.cdp.dao.TicketDaoTest;
import com.epam.cdp.dao.UserAccountDaoTest;
import com.epam.cdp.dao.UserDaoTest;
import com.epam.cdp.service.EventServiceTest;
import com.epam.cdp.service.TicketServiceTest;
import com.epam.cdp.service.UserServiceTest;
import com.epam.cdp.web.EventControllerTest;
import com.epam.cdp.web.TicketControllerTest;
import com.epam.cdp.web.UserControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Suite.class)
@ActiveProfiles(profiles = "dev")
@Suite.SuiteClasses({
        UserAccountDaoTest.class,
        EventDaoTest.class,
        TicketDaoTest.class,
        UserDaoTest.class,
        EventServiceTest.class,
        TicketServiceTest.class,
        UserServiceTest.class,
        IntegrationTest.class,
        EventControllerTest.class,
        TicketControllerTest.class,
        UserControllerTest.class
})
public class TestSuite {
}