package batulin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import batulin.controller.ControllerTest;
import batulin.service.UserServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ControllerTest.class,
        UserServiceTest.class
})
public class TestSuite {
}
