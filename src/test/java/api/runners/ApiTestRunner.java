package api.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/api",
        glue = "api.stepdefinitions",
        plugin = {"pretty", "html:build/reports/cucumber/cucumber-api.html", "json:build/reports/cucumber/cucumber-api.json"}
)
public class ApiTestRunner {
}