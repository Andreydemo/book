package com.epam.cdp.batulin;

import com.epam.cdp.batulin.controller.NoteControllerTest;
import com.epam.cdp.batulin.controller.UserControllerTest;
import com.epam.cdp.batulin.service.NoteServiceTest;
import com.epam.cdp.batulin.service.UserServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NoteControllerTest.class,
        UserControllerTest.class,
        UserServiceTest.class,
        NoteServiceTest.class
})
public class TestSuite {
}
