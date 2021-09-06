package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/"},
        monochrome = true,
        plugin = {"html:target/reports/test-report/cucumber-html-report.html", "json:target/cucumber.json" },
        glue={"steps"})
public class runTest {}