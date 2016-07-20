package batulin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import batulin.controller.TimelineControllerTest;
import batulin.service.NoteServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TimelineControllerTest.class,
        NoteServiceTest.class
})
public class TestSuite {
}
