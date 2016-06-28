package com.epam.cdp.batulin;

import com.epam.cdp.batulin.controller.NoteCommandControllerTest;
import com.epam.cdp.batulin.controller.QueryControllerTest;
import com.epam.cdp.batulin.controller.UserCommandControllerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NoteCommandControllerTest.class,
        QueryControllerTest.class,
        UserCommandControllerTest.class,
})
public class TestSuite {
}