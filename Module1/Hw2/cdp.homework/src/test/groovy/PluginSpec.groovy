import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class PluginSpec extends Specification {
    @Shared
    def project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.epam.JsMinimizer'
    }

    def 'minimizing js from folder'() {
        setup:
        project.tasks.minifyJs.fileName = "allJs.js"
        project.tasks.minifyJs.inputFolder = "build\\resources\\test\\testFolder1"
        project.tasks.minifyJs.outputFolder = "build\\resources\\test\\testFolder1"
        when:
        project.tasks.minifyJs.execute()
        then:
        def file = new File("build\\resources\\test\\testFolder1\\allJs.js")
        assert file.exists()
        assert file.parentFile.listFiles().length == 1
    }

    def 'minimizing js from fileMap'() {
        setup:
        project.tasks.minifyJs.fileMap = [
                "build\\resources\\test\\testFolder2\\profile.js": ["build\\resources\\test\\testFolder2\\testCase1.js", "build\\resources\\test\\testFolder2\\testCase2.js"],
                "build\\resources\\test\\testFolder2\\registration.js": ["build\\resources\\test\\testFolder2\\testCase3.js", "build\\resources\\test\\testFolder2\\testCase4.js"]
        ]
        when:
        project.tasks.minifyJs.execute()
        then:
        def file = new File("build\\resources\\test\\testFolder2")
        assert file.listFiles().length == 2
        assert new File(file, "profile.js").exists()
        assert new File(file, "registration.js").exists()
    }

    def 'minimizing js with reg exp in it'() {
        setup:
        project.tasks.minifyJs.result = new StringBuffer()
        when:
        project.tasks.minifyJs.minimizeFile(new File("src/test/resources/js/testCase1.js"))
        then:
        assert project.tasks.minifyJs.result.toString() == new File("src/test/resources/minJs/testCase1.min.js").text
    }

    def 'minimizing js with strings with quotes in it'() {
        setup:
        project.tasks.minifyJs.result = new StringBuffer()
        when:
        project.tasks.minifyJs.minimizeFile(new File("src/test/resources/js/testCase2.js"))
        then:
        assert project.tasks.minifyJs.result.toString() == new File("src/test/resources/minJs/testCase2.min.js").text
    }

    def 'deleting multiline comments'() {
        setup:
        project.tasks.minifyJs.result = new StringBuffer()
        when:
        project.tasks.minifyJs.minimizeFile(new File("src/test/resources/js/testCase3.js"))
        then:
        assert project.tasks.minifyJs.result.toString() == new File("src/test/resources/minJs/testCase3.min.js").text
    }

    def 'deleting inline comments'() {
        setup:
        project.tasks.minifyJs.result = new StringBuffer()
        when:
        project.tasks.minifyJs.minimizeFile(new File("src/test/resources/js/testCase4.js"))
        then:
        assert project.tasks.minifyJs.result.toString() == new File("src/test/resources/minJs/testCase4.min.js").text
    }
}
